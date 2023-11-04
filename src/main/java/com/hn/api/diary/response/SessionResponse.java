package com.hn.api.diary.response;

import lombok.Getter;

@Getter
public class SessionResponse {

    private final String token;

    public SessionResponse(String token) {
        this.token = token;
    }
}
