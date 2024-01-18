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
//                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://main.du6t0lf6rbu8z.amplifyapp.com")
                .allowCredentials(true)
                .exposedHeaders("Set-Cookie")   // 클라이언트가 받을 수 있는,
                .allowedHeaders("Authorization");   // 클라이언트가 요청할 수 있는,
    }
}
