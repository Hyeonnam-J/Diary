package com.hn.api.diary.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.member.MyUserDetails;
import com.hn.api.diary.dto.member.SessionDTO;
import com.hn.api.diary.dto.member.SignInDTO;
import com.hn.api.diary.response.SessionResponse;
import com.hn.api.diary.util.JwsKey;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SignInFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private static final AntPathRequestMatcher SIGN_IN_REQUEST_MATCHER
            = new AntPathRequestMatcher("/signIn", "POST");

    public SignInFilter() {
        super(SIGN_IN_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SignInDTO signInDTO = objectMapper.readValue(request.getInputStream(), SignInDTO.class);
        String username = signInDTO.getEmail();
        String password = signInDTO.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 실행하면 리다이렉트가 된다. 여기서 바로 클라이언트로 리턴하기 위해서 삭제.
//        super.successfulAuthentication(request, response, chain, authResult);

        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();

        List<GrantedAuthority> authorities = new ArrayList<>(myUserDetails.getAuthorities());
        List<String> rolesList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String roles = String.join(",", rolesList);

        SessionDTO sessionDTO = SessionDTO.builder()
                .memberId(myUserDetails.getMemberId())
                .email(myUserDetails.getUsername())
                .nick(myUserDetails.getNick())
                .role(roles)
                .build();
        String jwtSubject = objectMapper.writeValueAsString(sessionDTO);

        Date generateDate = new Date();
        Date expireDate = new Date(generateDate.getTime() + (24 * 60 * 60 * 1000)); // 하루

        // todo: jws -> spring security oauth2
        String jws = Jwts.builder()
                .subject(jwtSubject)
                .claim("memberId", sessionDTO.getMemberId())
                .claim("email", sessionDTO.getEmail())
                .claim("nick", myUserDetails.getNick())
                .signWith(JwsKey.getJwsSecretKey())
                .issuedAt(generateDate)
                .expiration(expireDate)
                .compact();

        // 쿠키 추가
        ResponseCookie cookie = ResponseCookie.from("jws", jws)
                .path("/")
                .sameSite("Strict")
                .maxAge((int) (expireDate.getTime() - generateDate.getTime()) / 1000)
//                .domain(".my-diary.life")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String body = objectMapper.writeValueAsString(SessionResponse.builder()
                .status(HttpURLConnection.HTTP_OK)
                .build()
        );
        response.getWriter().write(body);
    }
}
