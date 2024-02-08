package com.hn.api.diary.dto.member;

import com.hn.api.diary.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO extends Member {
    private Long id;
    private String email;
    private String nick;
}
