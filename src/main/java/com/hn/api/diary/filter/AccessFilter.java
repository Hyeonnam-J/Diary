package com.hn.api.diary.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.util.JwsKey;
import com.hn.api.diary.dto.SessionDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AccessFilter extends OncePerRequestFilter {

    @Autowired private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // /signIn -> signInFilter()
        if(request.getRequestURI().equals("/signIn")){
            filterChain.doFilter(request, response);
            return;
        }

        String userId = request.getHeader("userId");
        String postDetailId = request.getHeader("postDetailId");
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);

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

            setSecurityContextHolder(request, response, filterChain, userId, postDetailId, sessionDTO.getEmail(), sessionDTO.getRole());
        }catch (IllegalArgumentException e){
            // jws == null,
            setSecurityContextHolder(request, response, filterChain, "NONE", "NONE", "NONE");
        }catch (SignatureException e){
            // jwtKey is invalid
            setSecurityContextHolder(request, response, filterChain, "NONE", "NONE", "NONE");
        }catch (Exception e){
            // todo: return errorResponse
            e.printStackTrace();
        }
    }

    private void setSecurityContextHolder(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String userId,
            String postDetailId,
            String principal,
            String... authorityList
            ) throws ServletException, IOException {

        request.setAttribute("userId", userId);
        request.setAttribute("postDetailId", postDetailId);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authorityList);
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
