package io.example.authorization.generator;

import io.example.authorization.domain.partner.dto.CreatePartner;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.repository.PartnerRepository;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
public class PartnerGenerator {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PartnerRepository partnerRepository;

    public CreatePartner buildPartnerSignUp(){
        String partnerId = "naver";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        return CreatePartner.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();
    }

    public PartnerEntity buildPartnerEntity(){
        CreatePartner createPartner = this.buildPartnerSignUp();
        PartnerEntity partnerEntity = this.modelMapper.map(createPartner, PartnerEntity.class);
        partnerEntity.signUp();
        return partnerEntity;
    }

    public PartnerEntity savedPartnerEntity(){
        return partnerRepository.save(this.buildPartnerEntity());
    }

}
