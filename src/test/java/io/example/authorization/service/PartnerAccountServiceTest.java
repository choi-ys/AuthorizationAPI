package io.example.authorization.service;

import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.partner.entity.PartnerAccountEntity;
import io.example.authorization.domain.partner.entity.PartnerRole;
import io.example.authorization.domain.partner.entity.PartnerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class PartnerAccountServiceTest {

    @Autowired
    PartnerAccountService partnerAccountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 생성 Service")
    public void savePartnerAccount(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        PartnerAccountEntity givenPartnerAccountEntity = PartnerAccountEntity.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        //when
        ProcessingResult processingResult = partnerAccountService.savePartnerAccount(givenPartnerAccountEntity);
        PartnerAccountEntity createdPartnerAccountEntity = (PartnerAccountEntity) processingResult.getData();

        //then
        // partnerNo항목 Auto Increment 적용 여부 확인
        assertThat(createdPartnerAccountEntity.getPartnerNo()).isEqualTo(1);
        assertThat(createdPartnerAccountEntity.getPartnerId().equals(givenPartnerAccountEntity.getPartnerId()));

        // partnerPassword항목 암호화 적용 여부 확인
        assertThat(this.passwordEncoder.matches(givenPartnerAccountEntity.getPartnerPassword(), createdPartnerAccountEntity.getPartnerPassword()));
        assertThat(createdPartnerAccountEntity.getPartnerEmail().equals(givenPartnerAccountEntity.getPartnerEmail()));
        assertThat(createdPartnerAccountEntity.getPartnerCompanyName().equals(givenPartnerAccountEntity.getPartnerEmail()));

        // 사용자 계정 생성 시 초기값 적용 여부확인
        assertThat(createdPartnerAccountEntity.getRoles().equals(PartnerRole.FORBIDDEN));
        assertThat(createdPartnerAccountEntity.getStatus().equals(PartnerStatus.API_NOT_AVAILABLE));
    }
}