package io.example.authorization.service;

import io.example.authorization.domain.client.dto.ClientPublish;
import io.example.authorization.domain.client.entity.ClientDetailsEntity;
import io.example.authorization.domain.common.Error;
import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.domain.partner.entity.PartnerRole;
import io.example.authorization.repository.ClientDetailRepository;
import io.example.authorization.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService implements UserDetailsService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientDetailRepository clientDetailRepository;
    private final ModelMapper modelMapper;

    /** 용석:2021-01-18
     * 사용자 계정 생성 로직
     * @param partnerEntity
     * @return {@link ProcessingResult}
     */
    public ProcessingResult savePartner(PartnerEntity partnerEntity){
        // ID 항목 중복 검사
        if(this.isDuplicatedId(partnerEntity.getPartnerId())){
            return new ProcessingResult(Error.builder()
                    .code(-1)
                    .message("이미 존재하는 ID 입니다.")
                    .build()
            );
        }
        
        // 사용자 계정 생성 시 초기값으로 설정되어야 하는 항목들의 값 설정
        partnerEntity.signUp();

        // 비밀번호 항목 암호화 : Bcript 단방향 암호화 적용
        partnerEntity.setPartnerPassword(passwordEncoder.encode(partnerEntity.getPartnerPassword()));

        try {
            // 사용자 계정 DB 저장 및 처리 결과 반환
            return new ProcessingResult(this.partnerRepository.save(partnerEntity));
        } catch (Exception e) {
            // 로직 처리 중 발생하는 예외 반환 처리
            return this.serverError(e);
        }
    }

    /**
     * 회원 가입 이후 API 통신 권한 획득에 필요한 Client id/secret 정보 발급
     * @param clientPublish
     * @return
     */
    @Transactional
    public ProcessingResult createClientDetail(ClientPublish clientPublish){
        Optional<PartnerEntity> optionalPartnerAccountEntity = partnerRepository.findByPartnerNo(clientPublish.getPartnerNo());
        if(optionalPartnerAccountEntity.isEmpty()){
            return this.notFound();
        }

        PartnerEntity partnerEntity = optionalPartnerAccountEntity.get();
        if( (partnerEntity.getClientId() != null && !partnerEntity.getClientId().isBlank()) ){
            return new ProcessingResult(Error.builder()
                    .code(412) // 412 Precondition Failed : 전제 조건 실패 HttpStatusCode 반환
                    .message("Client 정보가 이미 발급 되었습니다.")
                    .build()
            );
        }

        ClientDetailsEntity clientDetailsEntity = this.modelMapper.map(clientPublish, ClientDetailsEntity.class);
        partnerEntity.publishedClientInfo();
        clientDetailsEntity.setPublishedClientInfo(partnerEntity);
        clientDetailRepository.save(clientDetailsEntity);
        return new ProcessingResult(clientDetailsEntity);
    }

    private boolean isDuplicatedId(String partnerId){
        long count = partnerRepository.countByPartnerId(partnerId);
        return (count == 0) ? false : true;
    }

    private ProcessingResult serverError(Exception e){
        return new ProcessingResult(Error.builder()
                .code(500)
                .message("요청을 처리하는 과정에서 오류가 발생하였습니다. 관리자에게 문의하세요.")
                .detail(e.getMessage())
                .build()
        );
    }

    private ProcessingResult notFound(){
        return new ProcessingResult(Error.builder()
                .code(404)
                .message("존재 하지 않는 회원 입니다.")
                .build()
        );
    }

    /**
     * Application에서 정의한 Account domain을 Spring security에서 정의한 UsertDetail Interface로 변환
     * @param partnerId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String partnerId) throws UsernameNotFoundException {
        PartnerEntity partnerAccountEntity = partnerRepository.findByPartnerId(partnerId)
                /**
                 * 요청 파라미터에 해당하는 Account 객체 조회 실패 시 오류 반환 처리
                 * - userName(account.email)에 해당 하는 Account 객체 조회 실패 시 Null을 반환 하므로
                 * - Srping security의 UsernameNotFoundException객체를 통해 정의된 오류를 반환한다.
                 */
                .orElseThrow(() -> new UsernameNotFoundException(partnerId));

        /**
         * Srping security의 User객체를 이용하여 MemberEntity객체를 UserDetails 객체로 변환
         *  - UserDetails interface로 객체 변환 처리를 구현할 경우 모든 메소드를 구현 해야하므로,
         *  - UserDetails의 User객체를 이용하여 MemberEntity 체를 Spring Security의 UserDetails 객체로 변환한다.
         */
        return new User(partnerAccountEntity.getPartnerId(),
                partnerAccountEntity.getPartnerPassword(),
                this.roleToAuthorities(partnerAccountEntity.getRoles())
        );
    }

    /**
     * Set으로 정의된 Role을 Collection Type으로 변환
     * @param roles
     * @return
     */
    private Collection<? extends GrantedAuthority> roleToAuthorities(Set<PartnerRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
