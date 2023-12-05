package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardPostReplyDTO {

    private String title;
    private String content;

    @Builder
    public BoardPostReplyDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
