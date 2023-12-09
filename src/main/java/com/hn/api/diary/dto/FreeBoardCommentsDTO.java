package com.hn.api.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FreeBoardCommentsDTO {

    private Long id;
    private UserDTO user;
    private FreeBoardPostDTO post;
    private String content;
    private String createdDate;
    private int depth;

}
