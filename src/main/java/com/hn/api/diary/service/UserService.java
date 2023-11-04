package com.hn.api.diary.service;

import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.MySession;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignUpDTO signUpDTO){

        // todo: 비밀번호 암호화.
        User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public User signIn(SignInDTO signInDTO) {
        String receivedEmail = signInDTO.getEmail();
        String receivedPassword = signInDTO.getPassword();

        User user = userRepository.findByEmailAndPassword(receivedEmail, receivedPassword)
                .orElseThrow(InvalidValue::new);

        // todo: Transactional이 없으면 addSession 실패. 이유 공부.
//        MySession mySession = user.addSession();
        return user;
    }
}
