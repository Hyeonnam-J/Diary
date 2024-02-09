package com.hn.api.diary.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpDTO {

    private String email;
    private String password;
    private String memberName;
    private String nick;
    private String phoneNumber;
    private final String role = "USER";

    @Builder // for testcode
    public SignUpDTO(String email, String password, String memberName, String nick, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.nick = nick;
        this.phoneNumber = phoneNumber;
    }
}
