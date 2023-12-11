package com.hn.api.diary.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor  // for testCode
@Getter
public class CheckDuplicationDTO {

    private String item;
    private String value;

    @Builder    // for testCode
    public CheckDuplicationDTO(String item, String value) {
        this.item = item;
        this.value = value;
    }
}
