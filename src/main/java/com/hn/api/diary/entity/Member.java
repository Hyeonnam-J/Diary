package com.hn.api.diary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String memberName;
    private String nick;
    private String phoneNumber;
    private String role;

    @Builder
    public Member(String email, String password, String memberName, String nick, String phoneNumber, String role) {
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.nick = nick;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
