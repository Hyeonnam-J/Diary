package com.hn.api.diary.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.repository.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("TDD - signUp")
    void signUp() throws Exception {
        // given
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test-signUp-email")
                .password("test-signUp-password")
                .build();

        // MockMvc content 파라미터로 String을 보내야 해서 직렬화 로직 추가.
        String json = objectMapper.writeValueAsString(signUpDTO);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        Assertions.assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("TDD - signIn")
    void SignIn() throws Exception{
        // given

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("asd")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        new Exception();
    }

}