package com.hn.api.diary.dto.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


public class MyUserDetails extends User {
    private final Long memberId;
    private final String nick;

    public MyUserDetails(UserDetails userDetails, Long userId, String nick) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.memberId = userId;
        this.nick = nick;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNick() {
        return nick;
    }
}
