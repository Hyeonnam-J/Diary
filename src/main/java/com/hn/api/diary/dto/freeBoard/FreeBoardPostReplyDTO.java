package com.hn.api.diary.dto.freeBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardPostReplyDTO {

    private String postId;
    private String title;
    private String content;

    @Builder // for test code
    public FreeBoardPostReplyDTO(String postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
