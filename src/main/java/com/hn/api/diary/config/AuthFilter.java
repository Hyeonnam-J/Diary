package com.hn.api.diary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SignInDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired private ObjectMapper objectMapper;

    public AuthFilter(String signInUrl) {
        super(signInUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        System.out.println("여기는 class's AuthFilter");
        SignInDTO signInDTO = objectMapper.readValue(request.getInputStream(), SignInDTO.class);

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                signInDTO.getEmail(),
                signInDTO.getPassword()
        );

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }
}
