package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    // todo: 컨트롤러, 서비스 분리

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;
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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());

        // expected
        Assertions.assertEquals(1, userRepository.count());
        User user = userRepository.findAll().iterator().next();
        Assertions.assertEquals(signUpDTO.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("DisplayName : duplicated email while signUp")
    void duplicatedEmailWhileSignUp() throws Exception {
        // given
        User user = User.builder()
                .email("test-signUp-email")
                .password("any")
                .build();
        userRepository.save(user);

        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test-signUp-email")
                .password("test-signUp-password")
                .build();

        // MockMvc content 파라미터로 String을 보내야 해서 직렬화 로직 추가.
        String json = objectMapper.writeValueAsString(signUpDTO);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isAlreadyReported())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("DisplayName : check crypto password in sign up")
    void checkCryptoPasswordInSignUp() throws Exception {
        // given
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("test-signUp-email")
                .password("test-signUp-password")
                .build();

        // MockMvc content 파라미터로 String을 보내야 해서 직렬화 로직 추가.
        String json = objectMapper.writeValueAsString(signUpDTO);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        User user = userRepository.findByEmail(signUpDTO.getEmail())
                        .orElseThrow(InvalidValue::new);

        Assertions.assertNotEquals(signUpDTO.getPassword(), user.getPassword());
        Assertions.assertNotNull(user.getPassword());
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

        userService.signUp(signUpDTO);

        User user = userRepository.findByEmail(signUpDTO.getEmail())
                .orElseThrow(InvalidValue::new);

        SignInDTO signInDTO = SignInDTO.builder()
                .email("test@naver.com")
                .password("!@#QWEasdzxc")
                .build();

        String json = objectMapper.writeValueAsString(signInDTO);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        MvcResult mvcResult = resultActions.andReturn();

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // expected
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