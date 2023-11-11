package com.hn.api.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.AuthSession;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.response.SessionResponse;
import com.hn.api.diary.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    @Autowired final private ObjectMapper objectMapper;

    // security 성공 후 여기로 리다이렉트?
    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

    @PostMapping(value = "/signUp")
    public void signUp(@RequestBody SignUpDTO signUpDTO){
        userService.signUp(signUpDTO);
    }

    @PostMapping(value = "/signIn")
    public ResponseEntity<SessionResponse> signIn(@RequestBody SignInDTO signInDTO) throws JsonProcessingException {
        User user = userService.signIn(signInDTO);

        SessionDTO sessionDTO = SessionDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        String jwtSubject = objectMapper.writeValueAsString(sessionDTO);

        SecretKey key = JwsKey.getJwsSecretKey();

        Date generateDate = new Date();
        Date expirateDate = new Date(generateDate.getTime() + (60 * 1000));

        String jws = Jwts.builder()
                .subject(jwtSubject)
                .signWith(key)
                .issuedAt(generateDate)
                .expiration(expirateDate)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, jws);

        SessionResponse body = new SessionResponse(jws);

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    @PostMapping(value = "/testAuth")
    public String testAuth(AuthSession authSession) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(authSession);
    }

    @PostMapping(value = "/postTest")
    public String postTest(){
        System.out.println("aa - postTest");
        return "aapostTest";
    }

    @PostMapping(value = "/postTest2")
    public String postTest2(){
        System.out.println("aa - postTest2");
        return "aapostTest2";
    }

    @GetMapping(value = "/test")
    public String test(){
        System.out.println("aa - test");
        return "aatest";
    }

    @GetMapping(value = "/success")
    public String success(){
        System.out.println("aa - success");
        return "aasuccess";
    }

    @GetMapping(value = "/fail")
    public String fail(){
        System.out.println("aa - fail");
        return "aafail";
    }
}
