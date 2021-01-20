package io.example.authorization.controller;

import io.example.authorization.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API:Index")
class IndexControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("목차 API : API 목록 조회")
    public void index() throws Exception {
        //when
        ResultActions resultActions = this.mockMvc.perform(get("/api/index")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}
