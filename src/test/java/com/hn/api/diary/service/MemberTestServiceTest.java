package com.hn.api.diary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.member.CheckDuplicationDTO;
import com.hn.api.diary.dto.member.SignUpDTO;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.exception.AlreadyReported;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.FreeBoardCommentRepository;
import com.hn.api.diary.repository.FreeBoardPostRepository;
import com.hn.api.diary.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberTestServiceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
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

    // signUp() - start
    /* ********************************************************************************* */
    @Test
    @DisplayName("sign up : fail while check duplicated email")
    void failWhileCheckDuplicatedEmail() {
        // [given]

        // 데이터를 넣어두고,
        Member member = Member.builder()
                .email("testDuplicated")
                .password("any")
                .userName("userName")
                .build();
        userRepository.save(member);

        // signUp 로직을 실행하기 위한 파라미터 생성.
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("testDuplicated")
                .password("any")
                .userName("userName")
                .build();

        // [expected]
        Assertions.assertThrows(AlreadyReported.class, () -> userService.signUp(signUpDTO));
    }

    @Test
    @DisplayName("sign up : check encrypted password")
    void checkEncryptedPassword() {
        // [given]

        // 암호화하는 signUp 로직을 거쳐 저장하고,
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .email("any")
                .password("isEncrypted")
                .userName("userName")
                .build();
        userService.signUp(signUpDTO);

        // 저장된 데이터를 가져온다.
        Member member = userRepository.findByEmail("any")
                .orElseThrow(InvalidValue::new);

        // [expected]
        Assertions.assertTrue(passwordEncoder.matches(signUpDTO.getPassword(), member.getPassword()));
    }

    @Test
    @DisplayName("sign up : check duplicated value")
    public void checkDuplicatedValue() {
        // [given]
        Member member = Member.builder()
                .email("email@naver.com")
                .password("!@#123QWEqwe")
                .nick("nick")
                .build();
        userRepository.save(member);

        CheckDuplicationDTO checkDuplicationDTO_1 = CheckDuplicationDTO.builder()
                .item("email")
                .value("email@naver.com")
                .build();
        CheckDuplicationDTO checkDuplicationDTO_2 = CheckDuplicationDTO.builder()
                .item("nick")
                .value("nick")
                .build();

        // [expected]
        Assertions.assertThrows(AlreadyReported.class, () -> userService.checkDuplication(checkDuplicationDTO_1));
        Assertions.assertThrows(AlreadyReported.class, () -> userService.checkDuplication(checkDuplicationDTO_2));
    }
    /* ********************************************************************************* */
    // signUp() - end
}