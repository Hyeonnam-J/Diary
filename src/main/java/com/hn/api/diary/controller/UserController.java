package com.hn.api.diary.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.dto.user.CheckDuplicationDTO;
import com.hn.api.diary.dto.user.SignUpDTO;
import com.hn.api.diary.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signUp")
    public void signUp(@RequestBody SignUpDTO signUpDTO){
        userService.signUp(signUpDTO);
    }

    @PostMapping(value = "/signUp/checkDuplication")
    public void checkDuplication(@RequestBody CheckDuplicationDTO checkDuplicationDTO) {
        userService.checkDuplication(checkDuplicationDTO);
    }
}
