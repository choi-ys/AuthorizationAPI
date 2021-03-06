package io.example.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.authorization.domain.client.dto.CreateClient;
import io.example.authorization.domain.common.ProcessingResult;
import io.example.authorization.domain.common.resource.ErrorsEntityModel;
import io.example.authorization.domain.common.resource.ProcessingResultEntityModel;
import io.example.authorization.domain.partner.dto.CreatePartner;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final ModelMapper modelMapper;
    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity createPartner(@RequestBody @Valid CreatePartner createPartner, Errors errors) throws JsonProcessingException {
        if(errors.hasErrors()){
            return this.badRequest(errors);
        }

        PartnerEntity partnerEntity = this.modelMapper.map(createPartner, PartnerEntity.class);
        ProcessingResult processingResult = this.partnerService.savePartner(partnerEntity);

        if(processingResult.isSuccess()){
            return this.createResponse(processingResult);
        }else{
            return this.errorResponse(processingResult);
        }
    }

    @PostMapping("/client")
    public ResponseEntity createClientInfo(@RequestBody @Valid CreateClient createClient, Errors errors){
        if(errors.hasErrors()){
            return this.badRequest(errors);
        }

        ProcessingResult processingResult = this.partnerService.createClientDetail(createClient);
        if(processingResult.isSuccess()){
            return this.createResponse(processingResult);
        }else{
            return errorResponse(processingResult);
        }
    }

    /**
     * Service 처리 결과가 성공이 아닌경우 오류 응답 처리
     */
    private ResponseEntity errorResponse(ProcessingResult processingResult) {
        int errorCode = processingResult.getError().getCode();
        switch (errorCode){
            case 404:
                return this.notFoundWithBody(processingResult);
            case 412:
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(processingResult.getError());
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
        URI location = linkTo(PartnerController.class).withSelfRel().toUri();
        return ResponseEntity.created(location).body(new ProcessingResultEntityModel(processingResult));
    }

    public ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorsEntityModel(errors));
    }

    /**
     * 잘못된 요청의 Not Found 응답 처리
     * @return 404 Not Found
     */
    private ResponseEntity notFound() {
        URI indexUri = linkTo(methodOn(IndexController.class).index()).toUri();
        return ResponseEntity.notFound().location(indexUri).build();
    }

    private ResponseEntity notFoundWithBody(ProcessingResult processingResult) {
        URI indexUri = linkTo(methodOn(IndexController.class).index()).toUri();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(processingResult);
    }

}
