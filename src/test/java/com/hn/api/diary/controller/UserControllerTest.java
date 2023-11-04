package com.hn.api.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hn.api.diary.config.AuthSession;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.entity.MySession;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.SessionRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private SessionRepository sessionRepository;

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

    /**
     * 일단 쿠키를 이용한 인증처리 테스트는 주석.
     */
//    @Transactional  // todo : @Transactional을 뺄 수 있는 방법 고민.
//    @Test
//    @DisplayName("TDD - generate session after signIn success")
//    void generateSessionAfterSignIn() throws Exception{
//        // given
//        // save user entity and generate signInDTO
//        SignUpDTO signUpDTO = SignUpDTO.builder()
//                .email("test@naver.com")
//                .password("!@#QWEasdzxc")
//                .build();
//
//        User user = User.builder()
//                .email(signUpDTO.getEmail())
//                .password(signUpDTO.getPassword())
//                .build();
//
//        userRepository.save(user);
//
//        SignInDTO signInDTO = SignInDTO.builder()
//                .email("test@naver.com")
//                .password("!@#QWEasdzxc")
//                .build();
//
//        String json = objectMapper.writeValueAsString(signInDTO);
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        // then
//        User signInUser = userRepository.findById(user.getId())
//                .orElseThrow(InvalidValue::new);
//        Assertions.assertEquals(1L, signInUser.getMySessions().size());
//    }

//    @Test
//    @DisplayName("TDD - response session in cookie after signIn success")
//    void responseSessionInCookieAfterSignIn() throws Exception{
//        // given
//        // save user entity and generate signInDTO
//        SignUpDTO signUpDTO = SignUpDTO.builder()
//                .email("test@naver.com")
//                .password("!@#QWEasdzxc")
//                .build();
//
//        User user = User.builder()
//                .email(signUpDTO.getEmail())
//                .password(signUpDTO.getPassword())
//                .build();
//
//        userRepository.save(user);
//
//        SignInDTO signInDTO = SignInDTO.builder()
//                .email("test@naver.com")
//                .password("!@#QWEasdzxc")
//                .build();
//
//        String json = objectMapper.writeValueAsString(signInDTO);
//
//        // when
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//        // Get the response cookies
//        MockHttpServletResponse response = result.getResponse();
//        String cookieHeader = response.getHeader("Set-Cookie");
//
//        // Extract session ID from the cookie
//        String sessionId = null;
//        if (cookieHeader != null) {
//            String[] cookies = cookieHeader.split(";");
//            for (String cookie : cookies) {
//                if (cookie.startsWith("SESSION")) {
//                    sessionId = cookie.split("=")[1]; // Get the session ID value
//                    break;
//                }
//            }
//        }
//
//        // then
//        assertNotNull(cookieHeader);
//        assertNotNull(sessionId);
//    }
//
//    @Test
//    @DisplayName("enter page with authorization")
//    void enterPageWithAuthorization() throws Exception{
//        // given
//        User user = User.builder()
//                .email("test@google.com")
//                .password("!@#QWEQasdzxc")
//                .build();
//        userRepository.save(user);
//
//        // for signIn
//        SignInDTO signInDTO = SignInDTO.builder()
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .build();
//
//        String signInDTO_json = objectMapper.writeValueAsString(signInDTO);
//
//        // signIn and getCookie
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(signInDTO_json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//        MockHttpServletResponse response = result.getResponse();
//        Cookie[] cookies = response.getCookies();
//
//        AuthSession authSession = new AuthSession(1L);
//        String authSession_json = objectMapper.writeValueAsString(authSession);
//
//        // when
//        // try enter auth page
//        mockMvc.perform(MockMvcRequestBuilders.post("/sendCookie")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .cookie(cookies)
//                        .content(authSession_json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        // then
//    }
//
//    @Test
//    @DisplayName("enter page with unauthorization")
//    void enterPageWithUnAuthorization() throws Exception{
//        // given
//        User user = User.builder()
//                .email("test@google.com")
//                .password("!@#QWEQasdzxc")
//                .build();
//        userRepository.save(user);
//
//        // for signIn
//        SignInDTO signInDTO = SignInDTO.builder()
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .build();
//
//        String signInDTO_json = objectMapper.writeValueAsString(signInDTO);
//
//        // signIn and getCookie
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(signInDTO_json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//        MockHttpServletResponse response = result.getResponse();
//        Cookie[] cookies = response.getCookies();
//
//        // insert wrong cookie's value
//        cookies[0].setValue(cookies[0].getValue() + "?");
//
//        AuthSession authSession = new AuthSession(1L);
//        String authSession_json = objectMapper.writeValueAsString(authSession);
//
//        // when
//        // try enter auth page
//        mockMvc.perform(MockMvcRequestBuilders.post("/sendCookie")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .cookie(cookies)
//                        .content(authSession_json))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andDo(MockMvcResultHandlers.print());
//
//        // then
//    }
    /* ********************************************************************************* */
    // signIn() - end
}