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

}
