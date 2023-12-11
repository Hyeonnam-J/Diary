package com.hn.api.diary.dto;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


public class MyUserDetails extends User {
    private final Long userId;
    private final String nick;

    public MyUserDetails(UserDetails userDetails, Long userId, String nick) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.userId = userId;
        this.nick = nick;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNick() {
        return nick;
    }
}
