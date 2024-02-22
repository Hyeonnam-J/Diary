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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
public class AccessFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private final static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("accessFilter, ip : {}", request.getRemoteAddr());
        logger.info("accessFilter, uri : {}", request.getRequestURI());

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
            logger.info("accessFilter, jws IllegalArgumentException");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();

            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } catch (SignatureException e) {
            // jwtKey is invalid
            logger.info("accessFilter, jws SignatureException");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();

            response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
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
