package com.hn.api.diary.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SessionDTO {

    private Long memberId;
    private String email;
    private String nick;
    private String role;

    @Builder
    public SessionDTO(Long memberId, String email, String nick, String role) {
        this.memberId = memberId;
        this.email = email;
        this.nick = nick;
        this.role = role;
    }
}