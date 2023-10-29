package com.hn.api.diary.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "sign")
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
