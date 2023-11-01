package com.hn.api.diary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String email;
    private String password;
    private LocalDateTime createdAt;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<MySession> mySessions = new ArrayList<>();

    public MySession addSession() {
        MySession mySession = MySession.builder()
                .user(this)
                .build();
        mySessions.add(mySession);
        return mySession;
    }
}
