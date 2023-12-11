package com.hn.api.diary.dto.freeBoard;

import com.hn.api.diary.dto.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FreeBoardPostsDTO {

    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String createdDate;
    private UserDTO user;   // modelMapper에서 변수명을 참조하기 때문에 user여야 한다.
    private int depth;
    
}
