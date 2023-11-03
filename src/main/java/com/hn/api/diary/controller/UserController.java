package com.hn.api.diary.controller;

import com.hn.api.diary.config.AuthSession;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

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
    public ResponseEntity<Object> signIn(@RequestBody SignInDTO signInDTO){
        String token = userService.signIn(signInDTO);

        ResponseCookie cookie = ResponseCookie.from("SESSION", token)
                .domain("localhost")    // todo 배포 전 수정.
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping(value = "/sendCookie")
    public String sendCookie(AuthSession authSession){
        System.out.println(authSession);
        return "권한이 필요한 게시물에 접근 성공";
    }

    @PostMapping(value = "/test")
    public String test(@CookieValue("SESSION") String test){
        System.out.println(test);
        System.out.println("test");
        return "test";
    }
}
