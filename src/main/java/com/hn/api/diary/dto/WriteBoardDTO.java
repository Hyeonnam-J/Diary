package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WriteBoardDTO {

    private String title;
    private String content;

    @Builder
    public WriteBoardDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
