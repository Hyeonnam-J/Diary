package com.hn.api.diary.config;

import com.hn.api.diary.dto.SessionDTO;

public class AuthSession {

    public final Long id;
    public final String email;

    public AuthSession(SessionDTO sessionDTO) {
        this.id = sessionDTO.getId();
        this.email = sessionDTO.getEmail();
    }
}
