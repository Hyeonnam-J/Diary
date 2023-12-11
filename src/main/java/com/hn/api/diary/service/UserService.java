package com.hn.api.diary.service;

import com.hn.api.diary.dto.user.CheckDuplicationDTO;
import com.hn.api.diary.dto.user.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.AlreadyReported;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpDTO signUpDTO){

        Optional<User> optionalUser = userRepository.findByEmailOrNick(signUpDTO.getEmail(), signUpDTO.getNick());
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }

        String encryptedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(encryptedPassword)
                .userName(signUpDTO.getUserName())
                .nick(signUpDTO.getNick())
                .phoneNumber(signUpDTO.getPhoneNumber())
                .role(signUpDTO.getRole())
                .build();

        userRepository.save(user);
    }

    public void checkDuplication(CheckDuplicationDTO checkDuplicationDTO){
        Optional<User> optionalUser = Optional.empty();

        switch (checkDuplicationDTO.getItem()){
            case "email":
                optionalUser = userRepository.findByEmail(checkDuplicationDTO.getValue());
                break;
            case "nick":
                optionalUser = userRepository.findByNick(checkDuplicationDTO.getValue());
                break;
        }

        // 중복 값이 존재하면 예외를 발생시키고 중복 값이 없으면 아무 처리도 하지 않아서 브라우저에서 200 반환되게끔.
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }
    }
}
