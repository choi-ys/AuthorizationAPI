package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.client.dto.CreateClient;
import io.example.authorization.domain.partner.dto.CreatePartner;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API:Partner[Fail]")
class PartnerControllerFailTest extends BaseTest {

    @Test
    @DisplayName("파트너 계정 생성 API : 값이 없는 요청")
    public void createPartnerAPI_EmptyParamRequest() throws Exception {
        //given
        CreatePartner createPartner = CreatePartner.builder()
                .build();

        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createPartner))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createPartner"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").doesNotExist())
        ;
    }

    @Test
    @DisplayName("파트너 계정 생성 API : 유효하지 못한 값의 요청")
    public void createPartnerAPI_WrongParamRequest() throws Exception {
        //given
        String partnerId = "a";
        String partnerPassword = "b";
        String partnerEmail = "c";
        String partnerCompanyName = "d";

        CreatePartner createPartner = CreatePartner.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

        //when
        String urlTemplate = "/api/partner";
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createPartner))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createPartner"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
        ;
    }

    @Test
    @DisplayName("클리이언트 정보 생성 API : 존재하지 않는 파트너의 클라이언트 정보 생성 요청")
    public void createClientInfo_NotExistsPartnerRequest() throws Exception {
        //given
        CreateClient createClient = CreateClient.builder()
                .partnerNo(9999999)
                .resourceIds("CloudM")
                .webServerRedirectUri("http://localhost:8080/oauth/callback")
                .build();
        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner/client/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createClient))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("error.code").value(404))
                .andExpect(jsonPath("error.message").isNotEmpty())
        ;
    }

    @Test
    @DisplayName("클라이언트 정보 생성 API : 값이 없는 요청")
    public void createClientInfo_EmptyParamRequest() throws Exception {
        //given
        PartnerEntity savedPartnerEntity = partnerGenerator.savedPartnerEntity();

        CreateClient createClient = CreateClient.builder()
                .build();
        //when
        String urlTemplate = "/api/partner/client/";
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createClient))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createClient"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").doesNotExist())
        ;
    }

    @Test
    @DisplayName("클라이언트 정보 생성 API : 유효하지 못한 값의 요청")
    public void createClientInfo_WrongParamRequest() throws Exception {
        //given
        PartnerEntity savedPartnerEntity = partnerGenerator.savedPartnerEntity();

        String resourceIds = "CloudM";
        String webServerRedirectUri = "oauth/callback";

        CreateClient createClient = CreateClient.builder()
                .partnerNo(savedPartnerEntity.getPartnerNo())
                .resourceIds(resourceIds)
                .webServerRedirectUri(webServerRedirectUri)
                .build();
        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner/client/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(createClient))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].objectName").value("createClient"))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
        ;
    }
}