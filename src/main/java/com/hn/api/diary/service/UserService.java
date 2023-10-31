package com.hn.api.diary.service;

import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignUpDTO signUpDTO){
        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .build();

        userRepository.save(user);
    }

    public void signIn(SignInDTO signInDTO) {
        String receivedEmail = signInDTO.getEmail();
        String receivedPassword = signInDTO.getPassword();

        User user = userRepository.findByEmailAndPassword(receivedEmail, receivedPassword)
                .orElseThrow(InvalidValue::new);
        
        System.out.println(user.getEmail());
    }
}
