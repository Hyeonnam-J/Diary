package com.hn.api.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.AuthSession;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.response.SessionResponse;
import com.hn.api.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signUp")
    public void signUp(@RequestBody SignUpDTO signUpDTO){
        userService.signUp(signUpDTO);
    }

    @PostMapping(value = "/signIn")
    public ResponseEntity<SessionResponse> signIn(@RequestBody SignInDTO signInDTO) throws JsonProcessingException {
        String jws = userService.signIn(signInDTO);

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

    @PostMapping(value = "/test")
    public String test(){
        System.out.println("test");
        return "test";
    }
}
