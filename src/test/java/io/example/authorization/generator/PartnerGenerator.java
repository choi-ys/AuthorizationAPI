package io.example.authorization.generator;

import io.example.authorization.domain.partner.dto.PartnerSignUp;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.repository.PartnerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class PartnerGenerator {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PartnerRepository partnerRepository;

    public PartnerSignUp buildPartnerSignUp(){
        String partnerId = "naver";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        return PartnerSignUp.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();
    }

    public PartnerEntity buildPartnerEntity(){
        PartnerSignUp partnerSignUp = this.buildPartnerSignUp();
        PartnerEntity partnerEntity = this.modelMapper.map(partnerSignUp, PartnerEntity.class);
        partnerEntity.signUp();
        return partnerEntity;
    }

    public PartnerEntity savedPartnerEntity(){
        return partnerRepository.save(this.buildPartnerEntity());
    }

}
