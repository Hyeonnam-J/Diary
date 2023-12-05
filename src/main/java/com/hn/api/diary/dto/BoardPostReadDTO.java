package com.hn.api.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // modelMapper
@NoArgsConstructor
public class BoardPostReadDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String createdDate;
    private UserDTO user;
}
