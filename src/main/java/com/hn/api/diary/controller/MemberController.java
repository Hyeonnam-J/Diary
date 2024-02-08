package com.hn.api.diary.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hn.api.diary.dto.member.CheckDuplicationDTO;
import com.hn.api.diary.dto.member.SignUpDTO;
import com.hn.api.diary.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/signUp")
    public void signUp(@RequestBody SignUpDTO signUpDTO){
        memberService.signUp(signUpDTO);
    }

    @PostMapping(value = "/signUp/checkDuplication")
    public void checkDuplication(@RequestBody CheckDuplicationDTO checkDuplicationDTO) {
        memberService.checkDuplication(checkDuplicationDTO);
    }
}
