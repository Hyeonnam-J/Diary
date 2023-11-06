package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.UserRepository;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    // signUp() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("DisplayName : signUp")
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
    /* ********************************************************************************* */
    // signUp() - end

    // signIn() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("DisplayName : signIn success")
    void signInSuccess() throws Exception{
        // given
        // save user entity and generate signInDTO
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test@naver.com")
                .password("!@#QWEasdzxc")
                .build();

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .build();

        userRepository.save(user);

        SignInDTO signInDTO = SignInDTO.builder()
                .email("test@naver.com")
                .password("!@#QWEasdzxc")
                .build();

        String json = objectMapper.writeValueAsString(signInDTO);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
    }

    @Test
    @DisplayName("DisplayName : generate jwt after signIn")
    void generateJwtAfterSignIn() throws Exception {
        // given
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test@naver.com")
                .password("!@#QWEasdzxc")
                .build();

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .build();

        userRepository.save(user);

        SignInDTO signInDTO = SignInDTO.builder()
                .email("test@naver.com")
                .password("!@#QWEasdzxc")
                .build();

        String json = objectMapper.writeValueAsString(signInDTO);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // then
        // 헤더에 담은 토큰 값.
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String jws = mockHttpServletResponse.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰 값 decode
        String jwtSubject = Jwts.parser()
                .verifyWith(JwsKey.getJwsSecretKey())
                .build()
                .parseSignedClaims(jws)
                .getPayload()
                .getSubject();
        SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

        // 토큰이 담고 있는 내용 검증.
        Assertions.assertEquals(user.getId(), sessionDTO.getId());
        Assertions.assertEquals(user.getEmail(), sessionDTO.getEmail());
    }
    /* ********************************************************************************* */
    // signIn() - end
}