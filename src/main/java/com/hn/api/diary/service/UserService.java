package com.hn.api.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.crypto.PasswordEncoder;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.dto.SignUpDTO;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.AlreadyReported;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired final private ObjectMapper objectMapper;

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

    public String signIn(SignInDTO signInDTO) throws JsonProcessingException {
        String receivedEmail = signInDTO.getEmail();
        String receivedPassword = signInDTO.getPassword();

        User user = userRepository.findByEmail(receivedEmail)
                .orElseThrow(InvalidValue::new);

        var isMatches = PasswordEncoder.matches(receivedPassword, user.getPassword());
        if(!isMatches)throw new InvalidValue();

        SessionDTO sessionDTO = SessionDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        String jwtSubject = objectMapper.writeValueAsString(sessionDTO);

        SecretKey key = JwsKey.getJwsSecretKey();

        Date generateDate = new Date();
        Date expirateDate = new Date(generateDate.getTime() + (60 * 1000));

        String jws = Jwts.builder()
                .subject(jwtSubject)
                .signWith(key)
                .issuedAt(generateDate)
                .expiration(expirateDate)
                .compact();

        return jws;
    }
}
