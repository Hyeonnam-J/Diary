package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.user.SignInDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreeBoardPostRepository freeBoardPostRepository;
    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @BeforeEach
    void clean() {
        freeBoardCommentRepository.deleteAll();
        freeBoardPostRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("check role with user")
    void user() throws Exception {
        // [given]
        User user1 = User.builder()
                .email("nami0879@naver.com")
                .password("!@#123QWEqwe")
                .userName("jhn")
                .nick("hn")
                .role("USER")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("nami0878@naver.com")
                .password("!@#123QWEqwe")
                .userName("jhn")
                .nick("hn")
                .role("ADMIN")
                .build();
        userRepository.save(user2);

        SignInDTO user1_signInDTO = SignInDTO.builder()
                .email(user1.getEmail())
                .password(user1.getPassword())
                .build();
        String user1_signInDTO_json = objectMapper.writeValueAsString(user1_signInDTO);

        SignInDTO user2_signInDTO = SignInDTO.builder()
                .email(user2.getEmail())
                .password(user2.getPassword())
                .build();
        String user2_signInDTO_json = objectMapper.writeValueAsString(user2_signInDTO);

        // [when]
        ResultActions user1_signInResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user1_signInDTO_json));
        Cookie[] user1_strCookie = user1_signInResultActions.andReturn().getResponse().getCookies();

        ResultActions user2_signInResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user2_signInDTO_json));
        Cookie[] user2_strCookie = user2_signInResultActions.andReturn().getResponse().getCookies();

        ResultActions user1_userResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .cookie(user1_strCookie));

        ResultActions user2_userResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .cookie(user2_strCookie));

        // [then]
        user1_userResultActions.andExpect(MockMvcResultMatchers.status().isOk());
        user2_userResultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("check role with admin")
    void admin() throws Exception {
        // [given]
        User user1 = User.builder()
                .email("nami0879@naver.com")
                .password("!@#123QWEqwe")
                .userName("jhn")
                .nick("hn")
                .role("USER")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("nami0878@naver.com")
                .password("!@#123QWEqwe")
                .userName("jhn")
                .nick("hn")
                .role("ADMIN")
                .build();
        userRepository.save(user2);

        SignInDTO user1_signInDTO = SignInDTO.builder()
                .email(user1.getEmail())
                .password(user1.getPassword())
                .build();
        String user1_signInDTO_json = objectMapper.writeValueAsString(user1_signInDTO);

        SignInDTO user2_signInDTO = SignInDTO.builder()
                .email(user2.getEmail())
                .password(user2.getPassword())
                .build();
        String user2_signInDTO_json = objectMapper.writeValueAsString(user2_signInDTO);

        // [when]
        ResultActions user1_signInResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user1_signInDTO_json));
        Cookie[] user1_strCookie = user1_signInResultActions.andReturn().getResponse().getCookies();

        ResultActions user2_signInResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user2_signInDTO_json));
        Cookie[] user2_strCookie = user2_signInResultActions.andReturn().getResponse().getCookies();

        ResultActions user1_userResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                .cookie(user1_strCookie));

        ResultActions user2_userResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                .cookie(user2_strCookie));

        // [then]
        user1_userResultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        user2_userResultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
