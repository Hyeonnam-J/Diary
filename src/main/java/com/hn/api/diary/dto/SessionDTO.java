package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SessionDTO {

    private String email;
    private String password;
    private String role;

    @Builder
    public SessionDTO(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}