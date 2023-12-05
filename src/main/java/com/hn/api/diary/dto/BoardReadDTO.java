package com.hn.api.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter // modelMapper
@NoArgsConstructor
public class BoardReadDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String createdDate;
    private UserDTO user;
    private List<CommentDTO> comments;
}
