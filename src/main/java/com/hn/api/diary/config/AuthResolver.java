package com.hn.api.diary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.api.diary.dto.SessionDTO;
import com.hn.api.diary.exception.Unauthorization;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    @Autowired final private ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        if(jws == null || jws.equals("")){
            throw new Unauthorization();
        }

        try {
            String jwtSubject = Jwts.parser()
                    .verifyWith(JwsKey.getJwsSecretKey())
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload()
                    .getSubject();

            SessionDTO sessionDTO = objectMapper.readValue(jwtSubject, SessionDTO.class);

            return new AuthSession(sessionDTO);
        } catch (JwtException e) {
            throw new Unauthorization();
        }
    }
}
