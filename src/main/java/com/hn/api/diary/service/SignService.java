package com.hn.api.diary.service;

import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.Sign;
import com.hn.api.diary.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignService {

    private final SignRepository signRepository;

    public void signUp(SignUpDTO signUpDTO){
        Sign sign = Sign.builder()
                .userId(signUpDTO.getUserId())
                .password(signUpDTO.getPassword())
                .build();

        signRepository.save(sign);
    }
}
