package com.hn.api.diary.dto.freeBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardPostWriteDTO {

    private String title;
    private String content;

    @Builder
    public FreeBoardPostWriteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
