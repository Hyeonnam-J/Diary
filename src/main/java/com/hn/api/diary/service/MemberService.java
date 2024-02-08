package com.hn.api.diary.service;

import com.hn.api.diary.dto.member.CheckDuplicationDTO;
import com.hn.api.diary.dto.member.SignUpDTO;
import com.hn.api.diary.entity.Member;
import com.hn.api.diary.exception.AlreadyReported;
import com.hn.api.diary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpDTO signUpDTO){

        Optional<Member> optionalUser = memberRepository.findByEmailOrNick(signUpDTO.getEmail(), signUpDTO.getNick());
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }

        String encryptedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        Member member = Member.builder()
                .email(signUpDTO.getEmail())
                .password(encryptedPassword)
                .userName(signUpDTO.getUserName())
                .nick(signUpDTO.getNick())
                .phoneNumber(signUpDTO.getPhoneNumber())
                .role(signUpDTO.getRole())
                .build();

        memberRepository.save(member);
    }

    public void checkDuplication(CheckDuplicationDTO checkDuplicationDTO){
        Optional<Member> optionalUser = Optional.empty();

        switch (checkDuplicationDTO.getItem()){
            case "email":
                optionalUser = memberRepository.findByEmail(checkDuplicationDTO.getValue());
                break;
            case "nick":
                optionalUser = memberRepository.findByNick(checkDuplicationDTO.getValue());
                break;
        }

        // 중복 값이 존재하면 예외를 발생시키고 중복 값이 없으면 아무 처리도 하지 않아서 브라우저에서 200 반환되게끔.
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }
    }
}
