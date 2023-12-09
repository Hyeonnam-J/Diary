package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.repository.CommentRepository;
import com.hn.api.diary.repository.PostRepository;
import com.hn.api.diary.util.JwsKey;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.UserRepository;
import com.hn.api.diary.service.UserService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @BeforeEach
    void clean(){
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    // signUp() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("sign up : success")
    void signUpSuccess() throws Exception {
        // [given]
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test-signUp-email")
                .password("test-signUp-password")
                .userName("test-signUp-userName")
                .build();

        // MockMvc content 파라미터로 String을 보내야 해서 직렬화 로직 추가.
        String signUpDTO_json = objectMapper.writeValueAsString(signUpDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpDTO_json));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        Assertions.assertEquals(signUpDTO.getEmail(), user.getEmail());
    }
    /* ********************************************************************************* */
    // signUp() - end

    // signIn() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("sign in : check accessToken after sign in")
     void checkAccessTokenAfterSignIn() throws Exception{
        // [given]
        // signUp 로직을 통해 암호화 된 데이터를 넣어두고,
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test")
                .password("test")
                .userName("test")
                .build();
        userService.signUp(signUpDTO);

        // mockMvc를 통해서 보낼 파라미터 생성.
        SignInDTO signInDTO = SignInDTO.builder()
                .email("test")
                .password("test")
                .build();
        String signInDTO_json = objectMapper.writeValueAsString(signInDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInDTO_json));
        MvcResult mvcResult = resultActions.andReturn();

        // 헤더의 Authorization의 토큰 파싱.
        String jws = mvcResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        String jwtSubject = Jwts.parser()
                .verifyWith(JwsKey.getJwsSecretKey())
                .build()
                .parseSignedClaims(jws)
                .getPayload()
                .getSubject();

        SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

        // 토큰 파싱 후 값 비교를 위해.
        User user = userRepository.findByEmail(signUpDTO.getEmail())
                        .orElseThrow(InvalidValue::new);

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(user.getEmail(), sessionDTO.getEmail());
    }
    /* ********************************************************************************* */
    // signIn() - end
}