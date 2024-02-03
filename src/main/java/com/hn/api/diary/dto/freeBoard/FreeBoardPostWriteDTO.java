package com.hn.api.diary.dto.freeBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeBoardPostWriteDTO {

    @NotBlank(message = "Enter the title")
    private String title;
    private String content;

    @Builder // for test code
    public FreeBoardPostWriteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
