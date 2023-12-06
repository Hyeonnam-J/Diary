package com.hn.api.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardCommentsDTO {

    private Long id;
    private UserDTO user;
    private PostDTO post;
    private String content;
    private String createdDate;
    private int depth;

}
