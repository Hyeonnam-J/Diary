//package com.hn.api.diary.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hn.api.diary.dto.SignInDTO;
//import com.hn.api.diary.dto.SignUpDTO;
//import com.hn.api.diary.entity.User;
//import com.hn.api.diary.exception.AlreadyReported;
//import com.hn.api.diary.exception.InvalidValue;
//import com.hn.api.diary.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class UserServiceTest {
//
//    @Autowired private UserService userService;
//    @Autowired private UserRepository userRepository;
//    @Autowired private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void clean(){
//        userRepository.deleteAll();
//    }
//
//    // signUp() - start
//    /* ********************************************************************************* */
//    @Test
//    @DisplayName("sign up : fail while check duplicated email")
//    void failWhileCheckDuplicatedEmail(){
//        // [given]
//
//        // 데이터를 넣어두고,
//        User user = User.builder()
//                .email("testDuplicated")
//                .password("any")
//                .build();
//        userRepository.save(user);
//
//        // signUp 로직을 실행하기 위한 파라미터 생성.
//        SignUpDTO signUpDTO = SignUpDTO.builder()
//                .email("testDuplicated")
//                .password("any")
//                .build();
//
//        // [expected]
//        Assertions.assertThrows(AlreadyReported.class, () -> userService.signUp(signUpDTO));
//    }
//
//    @Test
//    @DisplayName("sign up : check encrypted password")
//    void checkEncryptedPassword(){
//        // [given]
//
//        // 암호화하는 signUp 로직을 거쳐 저장하고,
//        SignUpDTO signUpDTO = SignUpDTO.builder()
//                .email("any")
//                .password("isEncrypted")
//                .build();
//        userService.signUp(signUpDTO);
//
//        // 저장된 데이터를 가져온다.
//        User user = userRepository.findByEmail("any")
//                        .orElseThrow(InvalidValue::new);
//
//        // [expected]
//        Assertions.assertTrue(PasswordEncoder.matches(signUpDTO.getPassword(), user.getPassword()));
//    }
//    /* ********************************************************************************* */
//    // signUp() - end
//
//    // signIn() - start
//    /* ********************************************************************************* */
//    @Test
//    @DisplayName("sign in : check account and password")
//    void checkAccountAndPassword() throws JsonProcessingException {
//        // [given]
//        // signUp 로직을 통해 암호화 후 저장.
//        SignUpDTO signUpDTO = SignUpDTO.builder()
//                .email("any")
//                .password("any")
//                .build();
//        userService.signUp(signUpDTO);
//
//        // signIn 로직을 실행하기 위한 파라미터 생성.
//        SignInDTO signInDTO = SignInDTO.builder()
//                .email("any2")
//                .password("any2")
//                .build();
//
//        // [expected]
//        Assertions.assertThrows(InvalidValue.class, () -> userService.signIn(signInDTO));
//    }
//    /* ********************************************************************************* */
//    // signIn() - end
//}