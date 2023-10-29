package com.hn.api.diary.controller;

import com.hn.api.diary.service.SignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class SignControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private SignService signService;

    @Test
    @DisplayName("TDD 준비")
    void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test?str=hn"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hn"))
                .andDo(MockMvcResultHandlers.print());
    }

}