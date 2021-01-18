package io.example.authorization.repository;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class PartnerRepositoryTest extends BaseTest {

    @Resource
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("사용자 계정 DB 저장")
    public void savePartnerEntity(){
        //given
        PartnerEntity partnerEntity = partnerGenerator.buildPartnerEntity();

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