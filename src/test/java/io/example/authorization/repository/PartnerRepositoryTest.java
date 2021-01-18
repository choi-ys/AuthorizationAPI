package io.example.authorization.repository;

import io.example.authorization.domain.partner.entity.PartnerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class PartnerRepositoryTest {

    @Resource
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("사용자 계정 DB 저장")
    public void savePartnerEntity(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        //when
        PartnerEntity savedPartnerEntity = partnerRepository.save(partnerEntity);

        //then
        assertThat(savedPartnerEntity).isNotNull();
        assertThat(savedPartnerEntity.getPartnerNo()).isNotEqualTo(0);
        assertThat(partnerEntity.getPartnerId()).isEqualTo(savedPartnerEntity.getPartnerId());
        assertThat(partnerEntity.getPartnerPassword()).isEqualTo(savedPartnerEntity.getPartnerPassword());
        assertThat(partnerEntity.getPartnerEmail()).isEqualTo(savedPartnerEntity.getPartnerEmail());
        assertThat(partnerEntity.getPartnerCompanyName()).isEqualTo(savedPartnerEntity.getPartnerCompanyName());
    }
}