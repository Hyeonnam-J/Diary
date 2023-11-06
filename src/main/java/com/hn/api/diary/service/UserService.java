package com.hn.api.diary.service;

import com.hn.api.diary.crypto.PasswordEncoder;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.AlreadyReported;
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

        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.getEmail());
        if(optionalUser.isPresent()){
            throw new AlreadyReported();
        }

        String encryptedPassword = PasswordEncoder.encrypt(signUpDTO.getPassword());

        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(encryptedPassword)
                .build();

        userRepository.save(user);
    }

    public User signIn(SignInDTO signInDTO) {
        String receivedEmail = signInDTO.getEmail();
        String receivedPassword = signInDTO.getPassword();

        User user = userRepository.findByEmail(receivedEmail)
                .orElseThrow(InvalidValue::new);

        var isMatches = PasswordEncoder.matches(receivedPassword, user.getPassword());
        if(!isMatches)throw new InvalidValue();

        return user;
    }
}
