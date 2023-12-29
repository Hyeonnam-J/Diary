package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.user.SignInDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("check role with user")
    void user() throws Exception {
        // [given]
        User user = User.builder()
                .email("nami0879@naver.com")
                .password("!@#123QWEqwe")
                .userName("jhn")
                .nick("hn")
                .role("user")
                .build();
        userRepository.save(user);

        SignInDTO signInDTO = SignInDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        String signInDTO_json = objectMapper.writeValueAsString(signInDTO);

        // [when]
        ResultActions resultActions_signIn = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signInDTO_json));
        String token = resultActions_signIn.andReturn().getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .header(HttpHeaders.AUTHORIZATION, token));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
