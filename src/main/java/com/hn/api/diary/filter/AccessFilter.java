package com.hn.api.diary.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.member.SessionDTO;
import com.hn.api.diary.response.ErrorResponse;
import com.hn.api.diary.util.JwsKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;

public class AccessFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("accessFilter before send next filter : "+request.getRequestURI());

        // If the uri's logic doesn't require cookies, doFilter()
        if (
                request.getRequestURI().startsWith("/freeBoard/posts") ||
                request.getRequestURI().startsWith("/freeBoard/post/read") ||

                request.getRequestURI().startsWith("/freeBoard/comments") ||

                request.getRequestURI().startsWith("/test") ||

                request.getRequestURI().startsWith("/signUp") ||
                request.getRequestURI().equals("/signIn")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("accessFilter after send next filter : "+request.getRequestURI());

        String jws = "";
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(int i = 0; i < cookies.length; i++){
                if(cookies[i].getName().equals("jws")){
                    jws = cookies[i].getValue();
                    break;
                }
            }
        }

        System.out.println("receive cookie-jws : "+jws);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(JwsKey.getJwsSecretKey())
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload();

            Date generateDate = claims.getIssuedAt();
            Date expireDate = claims.getExpiration();

            String jwtSubject = claims.getSubject();
            SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

            setSecurityContextHolder(request, response, filterChain, sessionDTO.getMemberId().toString(), sessionDTO.getEmail(), sessionDTO.getRole());
        } catch (IllegalArgumentException e) {
            // jws == null,
            System.out.println("jws IllegalArgumentException");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } catch (SignatureException e) {
            // jwtKey is invalid
            System.out.println("jws SignatureException");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } catch (Exception e) {
            // todo: return errorResponse
            e.printStackTrace();
        }
    }

    private void setSecurityContextHolder(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String memberId,
            String principal,
            String... authorityList
    ) throws ServletException, IOException {
        request.setAttribute("memberId", memberId);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authorityList);
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
