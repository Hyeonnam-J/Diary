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
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    @Autowired final private ObjectMapper objectMapper;

    private final UserService userService;

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
        return objectMapper.writeValueAsString(authSession);
    }

    @PostMapping(value = "/test")
    public String test(){
        System.out.println("test");
        return "test";
    }
}
