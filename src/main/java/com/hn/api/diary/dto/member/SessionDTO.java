package com.hn.api.diary.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SessionDTO {

    private Long userId;
    private String email;
    private String nick;
    private String role;

    @Builder
    public SessionDTO(Long userId, String email, String nick, String role) {
        this.userId = userId;
        this.email = email;
        this.nick = nick;
        this.role = role;
    }
}