package com.hn.api.diary.dto.freeBoard;

import com.hn.api.diary.dto.member.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FreeBoardCommentsDTO {

    private Long id;
    private MemberDTO user;
    private FreeBoardPostDTO post;
    private String content;
    private String createdDate;
    private boolean isParent;

}
