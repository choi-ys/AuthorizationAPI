package io.example.authorization.domain.partner.entity;

import io.example.authorization.domain.partner.dto.PartnerSignUp;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class PartnerAccountEntityTest {

    @Test
    public void modelMapperTest(){
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "";

        PartnerSignUp partnerSignUp = PartnerSignUp.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        ModelMapper modelMapper = new ModelMapper();

        //when
        PartnerAccountEntity partnerAccountEntity = modelMapper.map(partnerSignUp, PartnerAccountEntity.class);

        //then
        assertThat(partnerAccountEntity.getPartnerId()).isEqualTo(partnerId);
        assertThat(partnerAccountEntity.getPartnerPassword()).isEqualTo(partnerPassword);
        assertThat(partnerAccountEntity.getPartnerEmail()).isEqualTo(partnerEmail);
        assertThat(partnerAccountEntity.getPartnerCompanyName()).isEqualTo(partnerCompanyName);
    }
}