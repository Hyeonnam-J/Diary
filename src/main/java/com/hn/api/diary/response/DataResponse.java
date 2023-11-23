package com.hn.api.diary.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DataResponse<T> {

    private final int status = 200;
    private final List<T> data;

    @Builder
    public DataResponse(List<T> data) {
        this.data = data;
    }
}
