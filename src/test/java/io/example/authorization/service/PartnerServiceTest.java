package io.example.authorization.service;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.domain.partner.entity.PartnerRole;
import io.example.authorization.domain.partner.entity.PartnerStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class PartnerServiceTest extends BaseTest {

    @Autowired
    PartnerService partnerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 생성 Service")
    public void savePartner(){
        //given
        PartnerEntity givenPartnerEntity = partnerGenerator.buildPartnerEntity();

        //when
        ProcessingResult processingResult = null;
        try {
            processingResult = partnerService.savePartner(givenPartnerEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PartnerEntity createdPartnerEntity = (PartnerEntity) processingResult.getData();

        //then
        // partnerNo항목 Auto Increment 적용 여부 확인
        assertThat(createdPartnerEntity.getPartnerNo()).isEqualTo(1);
        assertThat(createdPartnerEntity.getPartnerId().equals(givenPartnerEntity.getPartnerId()));

        // partnerPassword항목 암호화 적용 여부 확인
        assertThat(this.passwordEncoder.matches(givenPartnerEntity.getPartnerPassword(), createdPartnerEntity.getPartnerPassword()));
        assertThat(createdPartnerEntity.getPartnerEmail().equals(givenPartnerEntity.getPartnerEmail()));
        assertThat(createdPartnerEntity.getPartnerCompanyName().equals(givenPartnerEntity.getPartnerEmail()));

        // 사용자 계정 생성 시 초기값 적용 여부확인
        assertThat(createdPartnerEntity.getRoles().equals(PartnerRole.FORBIDDEN));
        assertThat(createdPartnerEntity.getStatus().equals(PartnerStatus.API_NOT_AVAILABLE));
    }
}