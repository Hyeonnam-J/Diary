package com.hn.api.diary.controller;

import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.entity.MySession;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.SessionRepository;
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
    /* ********************************************************************************* */
    // signUp() - end

    // signIn() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("TDD - signIn success")
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
    @Transactional
    @DisplayName("TDD - generate session after signIn success")
    void generateSessionAfterSignIn() throws Exception{
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

        // TODO
        //  @Transactional이 문제의 소지가 있어 지우고 아래의 코드로 바뀌어야 함.
        //  현재 프록시를 늦게 가져오는 문제.
//        User signInUser = userRepository.findById(user.getId())
//                        .orElseThrow(InvalidValue::new);
//        Assertions.assertEquals(1L, signInUser.getSessions().size());

        // then
        Assertions.assertEquals(1L, user.getMySessions().size());
    }

    @Test
    @DisplayName("TDD - response session in cookie after signIn success")
    void responseSessionInCookieAfterSignIn() throws Exception{
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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // Get the response cookies
        MockHttpServletResponse response = result.getResponse();
        String cookieHeader = response.getHeader("Set-Cookie");

        // Extract session ID from the cookie
        String sessionId = null;
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                if (cookie.startsWith("SESSION")) {
                    sessionId = cookie.split("=")[1]; // Get the session ID value
                    break;
                }
            }
        }

        // then
        assertNotNull(cookieHeader);
        assertNotNull(sessionId);
    }

    // todo: 쿠키가 안 담김. 포스트맨으로 테스트 했을 때 성공.
    @Test
    @DisplayName("enter page with authorization")
    void enterPageWithAuthorization() throws Exception{
        // given
        User user = User.builder()
                .email("test@google.com")
                .password("!@#QWEQasdzxc")
                .build();
        userRepository.save(user);

        // for signIn
        SignInDTO signInDTO = SignInDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String json = objectMapper.writeValueAsString(signInDTO);

        // signIn and getCookie
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String cookieHeader = response.getHeader("Set-Cookie");
        String[] cookies = cookieHeader.split(";");

        // when
        // try enter auth page
        mockMvc.perform(MockMvcRequestBuilders.post("/sendCookie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Cookie", cookies[0])
                        .content("{ \"id\": 123 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
    }

    // todo: 쿠키가 안 담김. 포스트맨으로 테스트 했을 때 성공.
    //  enterPageWithAuthorization()에서 토큰 값만 살짝 수정해서 테스트.
    @Test
    @DisplayName("enter page with unauthorization")
    void enterPageWithUnAuthorization() throws Exception{
        // given
        User user = User.builder()
                .email("test@google.com")
                .password("!@#QWEQasdzxc")
                .build();
        MySession mySession = user.addSession();
        userRepository.save(user);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/sendCookie")
                        .header("Authorization", mySession.getToken()+"-other")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

        // then
    }
    /* ********************************************************************************* */
    // signIn() - end
}