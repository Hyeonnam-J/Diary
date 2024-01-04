package com.hn.api.diary.dto.freeBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardCommentUpdateDTO {
    private String commentId;
    private String content;

    @Builder    // for test code
    public FreeBoardCommentUpdateDTO(String commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
