package com.hn.api.diary.dto;

import com.hn.api.diary.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private UserDTO user;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
