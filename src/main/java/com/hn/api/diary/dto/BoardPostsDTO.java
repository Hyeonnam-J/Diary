package com.hn.api.diary.dto;

import com.hn.api.diary.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class BoardPostsDTO {

    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private boolean isDelete;
    private String createdDate;
    private UserDTO user;   // modelMapper에서 변수명을 참조하기 때문에 user여야 한다.
}
