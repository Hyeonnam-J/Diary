package com.hn.api.diary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpHeaders;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    public static final String CLIENT_IP = "http://localhost:3000";
    public static final String CLIENT_IP = "https://my-diary.life";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins(CLIENT_IP)
                .allowCredentials(true)
                .allowedHeaders("Authorization", "Content-Type", "userId");
    }
}
