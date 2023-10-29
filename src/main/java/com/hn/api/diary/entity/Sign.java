package com.hn.api.diary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Sign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userId;
    private String password;

    @Builder
    public Sign(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
