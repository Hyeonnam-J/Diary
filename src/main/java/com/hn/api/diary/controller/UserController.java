package com.hn.api.diary.controller;

import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void signIn(@RequestBody SignInDTO signInDTO){
        userService.signIn(signInDTO);
    }

}
