package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import io.example.authorization.config.CustomMediaTypeConstants;
import io.example.authorization.domain.partner.dto.PartnerSignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PartnerControllerFailTest extends BaseTest {

    @Test
    @DisplayName("사용자 계정 생성 API : 값이 없는 사용자 계정 생성 요청")
    public void createPartnerAPI_EmptyRequest() throws Exception {
        //given
        PartnerSignUp partnerSignUp = PartnerSignUp.builder()
                .build();

        //when
        ResultActions resultActions = this.mockMvc.perform(post("/api/partner")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(CustomMediaTypeConstants.HAL_JSON_UTF8_VALUE)
                .content(this.objectMapper.writeValueAsString(partnerSignUp))
        );

        //then
        resultActions.andDo(print());
    }
}