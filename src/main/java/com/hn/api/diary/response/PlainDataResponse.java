package com.hn.api.diary.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlainDataResponse<T> {

    private final int status;
    private final T data;

    @Builder
    public PlainDataResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
