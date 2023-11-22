package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SessionDTO {

    private String email;
    private String role;

    @Builder
    public SessionDTO(String email, String role) {
        this.email = email;
        this.role = role;
    }
}