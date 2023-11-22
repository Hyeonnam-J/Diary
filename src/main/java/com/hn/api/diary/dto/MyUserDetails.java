package com.hn.api.diary.dto;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


public class MyUserDetails extends User {
    private final Long userId;

    public MyUserDetails(UserDetails userDetails, Long userId) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
