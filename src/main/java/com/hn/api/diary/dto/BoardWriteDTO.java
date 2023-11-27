package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardWriteDTO {

    private String title;
    private String content;

    @Builder
    public BoardWriteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
