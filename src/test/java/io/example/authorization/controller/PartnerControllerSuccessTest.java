package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.client.dto.ClientPublish;
import io.example.authorization.domain.partner.dto.PartnerSignUp;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.generator.PartnerGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PartnerControllerSuccessTest extends BaseTest {

    @Resource
    PartnerGenerator partnerGenerator;

    @Test
    @DisplayName("사용자 계정 생성 API")
    public void createPartner() throws Exception {
        //given
        PartnerSignUp partnerSignUp = partnerGenerator.buildPartnerSignUp();

        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(partnerSignUp))
        );

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("_links").exists())
        ;
    }

    @Test
    @DisplayName("사용자 계정 생성 및 Client 정보 발급")
    public void publishedClientInfo() throws Exception {
        //given
        PartnerEntity savedPartnerEntity = partnerGenerator.savedPartnerEntity();

        ClientPublish clientPublish = ClientPublish.builder()
                .partnerNo(savedPartnerEntity.getPartnerNo())
                .resourceIds("CloudM")
                .webServerRedirectUri("http://localhost:8080/oauth/callback")
                .build();
        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner/client/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(clientPublish))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.clientId").exists())
                .andExpect(jsonPath("data.clientSecret").exists())
                .andExpect(jsonPath("data.webServerRedirectUri").exists())
        ;
    }

    @Test
    @DisplayName("생성된 사용자 계정의 Client 정보 발급")
    public void publishedClientInfo2() throws Exception {
        //given
        ClientPublish clientPublish = ClientPublish.builder()
                .partnerNo(1)
                .resourceIds("CloudM")
                .webServerRedirectUri("http://localhost:8080/oauth/callback")
                .build();
        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner/client/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(clientPublish))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.clientId").exists())
                .andExpect(jsonPath("data.clientSecret").exists())
                .andExpect(jsonPath("data.webServerRedirectUri").exists())
        ;
    }
}