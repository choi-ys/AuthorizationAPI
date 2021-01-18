package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.partner.dto.PartnerSignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PartnerControllerSuccessTest extends BaseTest {

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
}