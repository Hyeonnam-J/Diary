package com.hn.api.diary.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.config.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.response.SessionResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

public class AccessFilter extends OncePerRequestFilter {

    @Autowired private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // /signIn -> authFilter()
        if(request.getRequestURI().equals("/signIn")){
            filterChain.doFilter(request, response);
            return;
        }

        SessionResponse sessionResponse = objectMapper.readValue(request.getInputStream(), SessionResponse.class);
        String jws = sessionResponse.getAccessToken();

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(JwsKey.getJwsSecretKey())
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload();

            String jwtSubject = claims.getSubject();
            Date generateDate = claims.getIssuedAt();
            Date expirateDate = claims.getExpiration();

            SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

            System.out.println("성공");
            System.out.println(sessionDTO.getEmail());
            System.out.println(sessionDTO.getPassword());
            System.out.println(sessionDTO.getRole());
            System.out.println(generateDate);
            System.out.println(expirateDate);
        }catch (Exception e){
            System.out.println("실패");
        }
    }
}
