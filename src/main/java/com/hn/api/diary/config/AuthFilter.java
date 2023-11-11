package com.hn.api.diary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.dto.SignInDTO;
import com.hn.api.diary.response.SessionResponse;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired private ObjectMapper objectMapper;
    private static final String CONTENT_TYPE = "application/json";

    private static final AntPathRequestMatcher SIGN_IN_REQUEST_MATCHER
            = new AntPathRequestMatcher("/signIn", "POST");

    public AuthFilter() {
        super(SIGN_IN_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        SignInDTO signInDTO = objectMapper.readValue(request.getInputStream(), SignInDTO.class);
        String username = signInDTO.getEmail();
        String password = signInDTO.getPassword();

//        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
//                signInDTO.getEmail(),
//                signInDTO.getPassword()
//        );

        if (username == null || password == null) {
            throw new AuthenticationServiceException("DATA IS MISS");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        System.out.println("여기는 성공 : "+userDetails.getUsername());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("성공!!!");

        SignInDTO signInDTO = SignInDTO.builder()
                .email(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();

        String jwtSubject = objectMapper.writeValueAsString(signInDTO);
        SecretKey key = JwsKey.getJwsSecretKey();
        Date generateDate = new Date();
        Date expirateDate = new Date(generateDate.getTime() + (60 * 1000));

        String jws = Jwts.builder()
                .subject(jwtSubject)
                .signWith(key)
                .issuedAt(generateDate)
                .expiration(expirateDate)
                .compact();

        SessionResponse body = new SessionResponse(jws);

        response.addHeader(HttpHeaders.AUTHORIZATION, jws);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        System.out.println("실패");
        response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
    }
}
