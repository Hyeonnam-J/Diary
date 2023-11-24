package com.hn.api.diary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SessionResponse {

    private final int status;
    private final String accessToken;

    @Builder
    public SessionResponse(int status, String accessToken) {
        this.status = status;
        this.accessToken = accessToken;
    }
}
