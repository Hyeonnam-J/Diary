package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardUpdateDTO {

    private String title;
    private String content;

    @Builder
    public BoardUpdateDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
