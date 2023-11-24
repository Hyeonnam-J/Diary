package com.hn.api.diary.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ListDataResponse<T> {

    private final int status;
    private final List<T> data;

    @Builder
    public ListDataResponse(int status, List<T> data) {
        this.status = status;
        this.data = data;
    }
}
