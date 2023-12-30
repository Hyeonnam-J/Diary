package com.hn.api.diary.dto.freeBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardPostUpdateDTO {

    private String postId;
    private String title;
    private String content;

    @Builder // for test code
    public FreeBoardPostUpdateDTO(String postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
