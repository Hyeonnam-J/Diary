package com.hn.api.diary.controller;

import com.hn.api.diary.config.AuthSession;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.exception.UnAuthorization;
import com.hn.api.diary.response.ErrorResponse;
import com.hn.api.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void signIn(@RequestBody SignInDTO signInDTO){
        userService.signIn(signInDTO);
    }

    @GetMapping(value = "/test2")
    public String test2(AuthSession authSession){
        return "있음";
    }

}
