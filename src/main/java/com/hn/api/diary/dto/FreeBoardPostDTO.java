package com.hn.api.diary.dto;

import com.hn.api.diary.entity.FreeBoardPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeBoardPostDTO extends FreeBoardPost {
    private Long id;
}
