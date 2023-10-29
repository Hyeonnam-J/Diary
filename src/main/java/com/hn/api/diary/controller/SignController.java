package com.hn.api.diary.controller;

import com.hn.api.diary.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.dto.SignUpDTO;

@RequiredArgsConstructor
@RestController
public class SignController {

    private final SignService signService;

    @PostMapping(value = "/signUp")
    public void signUp(@RequestBody SignUpDTO signUpDTO){
        signService.signUp(signUpDTO);
    }
}
