package com.hn.api.diary.service;

import com.hn.api.diary.dto.sign.SignUpDTO;
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

        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.getEmail());
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }

        String encryptedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(encryptedPassword)
                .userName(signUpDTO.getUserName())
                .phoneNumber(signUpDTO.getPhoneNumber())
                .role(signUpDTO.getRole())
                .build();

        userRepository.save(user);
    }
}
