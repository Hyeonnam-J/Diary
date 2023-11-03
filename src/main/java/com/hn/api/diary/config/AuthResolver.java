package com.hn.api.diary.config;

import com.hn.api.diary.entity.MySession;
import com.hn.api.diary.exception.Unauthorization;
import com.hn.api.diary.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }

    /**
     * 아래는 쿠키를 이용한 인증처리.
     *
     * SameSite 기본 값 Lax.
     * Lax -> 다른 도메인으로의 GET방식 크로스 사이트 요청 시에 쿠키 전송.
     * None으로 바꾸려면 secure 속성을 설정해야 한다.
     * secure을 설정하면 https에서만 쿠키를 전송한다.
     * https를 사용하려면 웹 사이트에 SSL 또는 TLS 인증서가 설치되어 있어야 한다.
     */
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if(httpServletRequest == null){
//            throw new Unauthorization();
//        }
//
//        Cookie[] cookies = httpServletRequest.getCookies();
//        if(cookies.length == 0){
//            throw new Unauthorization();
//        }
//
//        String token = cookies[0].getValue();
//        System.out.println(token);
//        MySession mySessionToken = sessionRepository.findByToken(token)
//                .orElseThrow(Unauthorization::new);
//        // todo: 세션 디비의 토큰 삭제. 쿠키 만료 시간과 맞춤?
//
//        // AuthSession : 현재 검증한 토큰을 가진 유저의 아이디 값.
//        return new AuthSession(mySessionToken.getUser().getId());
//    }
}
