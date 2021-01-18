package io.example.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.partner.dto.PartnerSignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class PartnerAccountControllerSuccessTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 계정 생성 API")
    public void createPartnerAccount() throws Exception {
        //given
        String partnerId = "choi-ys";
        String partnerPassword = "password";
        String partnerEmail = "project.log.062@gmail.com";
        String partnerCompanyName = "naver";

        PartnerSignUp partnerSignUp = PartnerSignUp.builder()
                .partnerId(partnerId)
                .partnerPassword(partnerPassword)
                .partnerEmail(partnerEmail)
                .partnerCompanyName(partnerCompanyName)
                .build();

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