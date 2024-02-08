package com.hn.api.diary.dto.freeBoard;

import com.hn.api.diary.dto.member.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // modelMapper
@NoArgsConstructor
public class FreeBoardPostReadDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String createdDate;
    private MemberDTO member;
}
