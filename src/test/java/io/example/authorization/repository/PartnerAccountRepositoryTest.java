package io.example.authorization.repository;

import io.example.authorization.domain.partner.entity.PartnerAccountEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class PartnerAccountRepositoryTest {

    @Resource
    PartnerAccountRepository partnerAccountRepository;

    @Test
    @DisplayName("사용자 계정 DB 저장")
    public void savePartnerAccountEntity(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        PartnerAccountEntity partnerAccountEntity = PartnerAccountEntity.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        //when
        PartnerAccountEntity savedPartnerAccountEntity = partnerAccountRepository.save(partnerAccountEntity);

        //then
        assertThat(savedPartnerAccountEntity).isNotNull();
        assertThat(savedPartnerAccountEntity.getPartnerNo()).isNotEqualTo(0);
        assertThat(partnerAccountEntity.getPartnerId()).isEqualTo(savedPartnerAccountEntity.getPartnerId());
        assertThat(partnerAccountEntity.getPartnerPassword()).isEqualTo(savedPartnerAccountEntity.getPartnerPassword());
        assertThat(partnerAccountEntity.getPartnerEmail()).isEqualTo(savedPartnerAccountEntity.getPartnerEmail());
        assertThat(partnerAccountEntity.getPartnerCompanyName()).isEqualTo(savedPartnerAccountEntity.getPartnerCompanyName());
    }
}