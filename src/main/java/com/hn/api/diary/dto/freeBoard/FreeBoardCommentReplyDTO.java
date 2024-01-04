package com.hn.api.diary.dto.freeBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardCommentReplyDTO {
    private String commentId;
    private String content;

    @Builder
    public FreeBoardCommentReplyDTO(String commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
