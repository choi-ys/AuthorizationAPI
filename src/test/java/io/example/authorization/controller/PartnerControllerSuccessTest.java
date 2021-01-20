package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.client.dto.CreateClient;
import io.example.authorization.domain.partner.dto.CreatePartner;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.domain.partner.entity.PartnerRole;
import io.example.authorization.domain.partner.entity.PartnerStatus;
import io.example.authorization.generator.PartnerGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@DisplayName("Partner API [Success]")
@DisplayName("API:Partner[Success]")
class PartnerControllerSuccessTest extends BaseTest {

    @Resource
    PartnerGenerator partnerGenerator;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 사용자 계정 생성 API TestCase
     * - 목표 : 사용자의 입력값으로 사용자 계정 생성 여부를 확인
     * - 확인 사항
     *   - Http Header
     *     - StatusCode : 201
     *     - location : http://localhost/api/partner
     *     - content-Type : application/hal+json;charset=UTF-8
     *     - link : self, get
     *   - Http Body
     *     - success : true
     *     - data.status : api_not_available
     * @throws Exception
     */
    @Test
    @DisplayName("파트너 계정 생성 API")
    public void createPartner() throws Exception {
        //given
        CreatePartner createPartner = partnerGenerator.buildPartnerSignUp();

        String urlTemplate = "/api/partner";

        //when
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createPartner))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().stringValues(HttpHeaders.CONTENT_TYPE, CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.partnerId").value(createPartner.getPartnerId()))
                .andExpect(jsonPath("data.partnerEmail").value(createPartner.getPartnerEmail()))
                .andExpect(jsonPath("data.status").value(PartnerStatus.API_NOT_AVAILABLE.name()))
                ;
    }

    @Test
    @DisplayName("클라이언트 정보 생성 API")
    public void publishedClientInfo() throws Exception {
        //given
        PartnerEntity savedPartnerEntity = partnerGenerator.savedPartnerEntity();

        String resourceIds = "CloudM";
        String webServerRedirectUri = "http://localhost:8080/oauth/callback";
        CreateClient createClient = CreateClient.builder()
                .partnerNo(savedPartnerEntity.getPartnerNo())
                .resourceIds(resourceIds)
                .webServerRedirectUri(webServerRedirectUri)
                .build();

        String urlTemplate = "/api/partner/client/";

        //when
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createClient))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().stringValues(HttpHeaders.CONTENT_TYPE, CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("data.resourceIds").value(createClient.getResourceIds()))
                .andExpect(jsonPath("data.webServerRedirectUri").value(createClient.getWebServerRedirectUri()))
                .andExpect(jsonPath("data.clientId").isNotEmpty())
                .andExpect(jsonPath("data.clientSecret").isNotEmpty())
                .andExpect(jsonPath("data.clientSecretOrigin").isNotEmpty())
                .andExpect(jsonPath("data.scope").isNotEmpty())
                .andExpect(jsonPath("data.authorities").value(PartnerRole.TRIAL.name()))
        ;
    }
}