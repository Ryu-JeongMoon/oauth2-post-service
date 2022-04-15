package com.support.oauth2postservice.security.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenResponse {

  private final String idToken;
  private final String accessToken;
  private final String refreshToken;

  @Builder
  public TokenResponse(String idToken, String accessToken, String refreshToken) {
    this.idToken = idToken;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
