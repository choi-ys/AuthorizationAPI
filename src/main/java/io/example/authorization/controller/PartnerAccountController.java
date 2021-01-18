package io.example.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.common.resource.ErrorsEntityModel;
import io.example.authorization.domain.common.resource.ProcessingResultEntityModel;
import io.example.authorization.domain.partner.dto.PartnerSignUp;
import io.example.authorization.domain.partner.entity.PartnerAccountEntity;
import io.example.authorization.service.PartnerAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/partner")
@RequiredArgsConstructor
public class PartnerAccountController {

    private final ModelMapper modelMapper;
    private final PartnerAccountService partnerAccountService;

    @PostMapping
    public ResponseEntity createPartnerAccount(@RequestBody @Valid PartnerSignUp partnerSignUp, Errors errors) throws JsonProcessingException {
        if(errors.hasErrors()){
            return this.badRequest(errors);
        }

        PartnerAccountEntity partnerAccountEntity = this.modelMapper.map(partnerSignUp, PartnerAccountEntity.class);
        ProcessingResult processingResult = this.partnerAccountService.savePartnerAccount(partnerAccountEntity);

        if(processingResult.isSuccess()){
            return this.createResponse(processingResult);
        }else{
            return this.errorResponse(processingResult);
        }
    }

    /**
     * Service 처리 결과가 성공이 아닌경우 오류 응답 처리
     */
    private ResponseEntity errorResponse(ProcessingResult processingResult) {
        int errorCode = processingResult.getError().getCode();
        switch (errorCode){
            case 500:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(processingResult.getError());
            default:
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(processingResult.getError());
        }
    }

    /**
     * POST 요청의 created 응답 처리
     */
    private ResponseEntity<ProcessingResultEntityModel> createResponse(ProcessingResult processingResult) {
        URI location = linkTo(PartnerAccountController.class).withSelfRel().toUri();
        return ResponseEntity.created(location).body(new ProcessingResultEntityModel(processingResult));
    }

    public ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorsEntityModel(errors));
    }
}
