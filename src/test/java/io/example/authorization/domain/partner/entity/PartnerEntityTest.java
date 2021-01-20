package io.example.authorization.domain.partner.entity;

import io.example.authorization.domain.partner.dto.CreatePartner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain:Partner")
class PartnerEntityTest {

    @Test
    @DisplayName("PartnerSingUp -> PartnerEntity : ModelMapper Mapping")
    public void modelMapperTest(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "";

        CreatePartner createPartner = CreatePartner.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        ModelMapper modelMapper = new ModelMapper();

        //when
        PartnerEntity partnerEntity = modelMapper.map(createPartner, PartnerEntity.class);

        //then
        assertThat(partnerEntity.getPartnerId()).isEqualTo(partnerId);
        assertThat(partnerEntity.getPartnerPassword()).isEqualTo(partnerPassword);
        assertThat(partnerEntity.getPartnerEmail()).isEqualTo(partnerEmail);
        assertThat(partnerEntity.getPartnerCompanyName()).isEqualTo(partnerCompanyName);
    }
}