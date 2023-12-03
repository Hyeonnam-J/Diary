package com.hn.api.diary.dto;

import com.hn.api.diary.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO extends Post {
    private Long id;
}
