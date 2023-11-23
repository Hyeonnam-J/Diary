package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostBoardDTO {

    private String title;
    private String content;

    @Builder
    public PostBoardDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
