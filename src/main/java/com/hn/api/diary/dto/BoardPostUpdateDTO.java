package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardPostUpdateDTO {

    private String title;
    private String content;

    @Builder
    public BoardPostUpdateDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
