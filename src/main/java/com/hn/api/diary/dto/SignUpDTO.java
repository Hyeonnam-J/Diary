package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpDTO {

  private String userId;
  private String password;

  @Builder
  public SignUpDTO(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }
}
