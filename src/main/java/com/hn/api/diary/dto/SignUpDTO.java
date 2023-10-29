package com.hn.api.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpDTO {

  private String userId;
  private String password;

  /*
  테스트 코드에서 데이터를 주기 위한 용도.
  클래스에 빌더 어노테이션을 달면 다른 어노테이션들과 충돌이 있는 경우가 있다고 함.
   */
  @Builder
  public SignUpDTO(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }
}
