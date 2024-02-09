package com.hn.api.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.member.CheckDuplicationDTO;
import com.hn.api.diary.dto.member.SessionDTO;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.MemberRepository;
import com.hn.api.diary.util.JwsKey;
import com.hn.api.diary.dto.member.SignInDTO;
import com.hn.api.diary.dto.member.SignUpDTO;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.service.MemberService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private FreeBoardPostRepository freeBoardPostRepository;
    @Autowired private FreeBoardCommentRepository freeBoardCommentRepository;

    @BeforeEach
    void clean(){
        freeBoardCommentRepository.deleteAll();
        freeBoardPostRepository.deleteAll();
        memberRepository.deleteAll();
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
                .memberName("test-signUp-userName")
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

        Assertions.assertEquals(1, memberRepository.count());

        Member member = memberRepository.findAll().iterator().next();
        Assertions.assertEquals(signUpDTO.getEmail(), member.getEmail());
    }

    @Test
    @DisplayName("sign up : duplicated email")
    public void checkEmailDuplicate() throws Exception {
        // [given]
        Member member = Member.builder()
                .email("test@naver.com")
                .password("!@#QWE123qwe")
                .nick("testNick")
                .build();
        memberRepository.save(member);

        CheckDuplicationDTO checkDuplicationDTO = CheckDuplicationDTO.builder()
                .item("email")
                .value("test@naver.com")
                .build();
        String signUpDTO_json = objectMapper.writeValueAsString(checkDuplicationDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp/checkDuplication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpDTO_json));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isAlreadyReported())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("sign up : duplicated nick")
    public void checkNickDuplicate() throws Exception {
        // [given]
        Member member = Member.builder()
                .email("test@naver.com")
                .password("!@#QWE123qwe")
                .nick("testNick")
                .build();
        memberRepository.save(member);

        CheckDuplicationDTO checkDuplicationDTO = CheckDuplicationDTO.builder()
                .item("nick")
                .value("testNick")
                .build();
        String signUpDTO_json = objectMapper.writeValueAsString(checkDuplicationDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp/checkDuplication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpDTO_json));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isAlreadyReported())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("sign up : pass duplication email test")
    public void passDuplicationEmailTest() throws Exception {
        // [given]
        Member member = Member.builder()
                .email("test@naver.com")
                .password("!@#QWE123qwe")
                .nick("testNick")
                .build();
        memberRepository.save(member);

        CheckDuplicationDTO checkDuplicationDTO = CheckDuplicationDTO.builder()
                .item("email")
                .value("test@googlel.co.kr")
                .build();
        String signUpDTO_json = objectMapper.writeValueAsString(checkDuplicationDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp/checkDuplication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpDTO_json));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("sign up : pass duplication nick test")
    public void passDuplicationNickTest() throws Exception {
        // [given]
        Member member = Member.builder()
                .email("test@naver.com")
                .password("!@#QWE123qwe")
                .nick("testNick")
                .build();
        memberRepository.save(member);

        CheckDuplicationDTO checkDuplicationDTO = CheckDuplicationDTO.builder()
                .item("nick")
                .value("testNick2")
                .build();
        String signUpDTO_json = objectMapper.writeValueAsString(checkDuplicationDTO);

        // [when]
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signUp/checkDuplication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpDTO_json));

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
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
                .memberName("test")
                .build();
        memberService.signUp(signUpDTO);

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

        Cookie[] cookies = resultActions.andReturn().getResponse().getCookies();
        String jws = cookies[0].getValue();

        String jwtSubject = Jwts.parser()
                .verifyWith(JwsKey.getJwsSecretKey())
                .build()
                .parseSignedClaims(jws)
                .getPayload()
                .getSubject();

        SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

        // 토큰 파싱 후 값 비교를 위해.
        Member member = memberRepository.findByEmail(signUpDTO.getEmail())
                        .orElseThrow(InvalidValue::new);

        // [then]
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(member.getEmail(), sessionDTO.getEmail());
    }
    /* ********************************************************************************* */
    // signIn() - end
}