package com.hn.api.diary.service;

import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void signIn(SignInDTO signInDTO){
        String userIdOfClient = signInDTO.getEmail();
        String passwordOfClient = signInDTO.getPassword();

        User user = userRepository.findByEmail(userIdOfClient);

        String passwordOfDatabase = user.getPassword();

        if(passwordOfClient.equals(passwordOfDatabase)){
            // TODO response package
            System.out.println("성공");
        }else{

        }
    }
}
