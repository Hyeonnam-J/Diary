package com.hn.api.diary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                 .allowedOrigins("*")
//                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("https://main.du6t0lf6rbu8z.amplifyapp.com")
                .allowedHeaders("*")    // 클라이언트가 요청할 때 허용되는 헤더.
                .exposedHeaders("Authorization");   // 서버에서 응답했을 때 브라우저가 접근 가능한 헤더.
    }
}
