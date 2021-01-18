package io.example.authorization.service;

import io.example.authorization.domain.common.Error;
import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.partner.entity.PartnerAccountEntity;
import io.example.authorization.repository.PartnerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerAccountService {

    private final PartnerAccountRepository partnerAccountRepository;
    private final PasswordEncoder passwordEncoder;

    /** 용석:2021-01-18
     * 사용자 계정 생성 로직
     * @param partnerAccountEntity
     * @return {@link ProcessingResult}
     */
    public ProcessingResult savePartnerAccount(PartnerAccountEntity partnerAccountEntity){
        // ID 항목 중복 검사
        if(this.isDuplicatedId(partnerAccountEntity.getPartnerId())){
            return new ProcessingResult(Error.builder()
                    .code(-1)
                    .message("이미 존재하는 ID 입니다.")
                    .build()
            );
        }
        
        // 사용자 계정 생성 시 초기값으로 설정되어야 하는 항목들의 값 설정
        partnerAccountEntity.signUp();

        // 비밀번호 항목 암호화 : Bcript 단방향 암호화 적용
        partnerAccountEntity.setPartnerPassword(passwordEncoder.encode(partnerAccountEntity.getPartnerPassword()));

        try {
            // 사용자 계정 DB 저장 및 처리 결과 반환
            return new ProcessingResult(this.partnerAccountRepository.save(partnerAccountEntity));
        } catch (Exception e) {
            // 로직 처리 중 발생하는 예외 반환 처리
            return this.serverError(e);
        }
    }

    private boolean isDuplicatedId(String partnerId){
        long count = partnerAccountRepository.countByPartnerId(partnerId);
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
}