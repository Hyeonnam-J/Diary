package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SessionDTO {

    private Long id;
    private String email;

    @Builder
    public SessionDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
