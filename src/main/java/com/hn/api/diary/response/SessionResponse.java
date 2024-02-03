package com.hn.api.diary.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponse {

    private final int status;

    @Builder
    public SessionResponse(int status) {
        this.status = status;
    }
}
