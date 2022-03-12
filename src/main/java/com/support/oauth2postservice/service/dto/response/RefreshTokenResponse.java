package com.support.oauth2postservice.service.dto.response;

import com.support.oauth2postservice.domain.entity.RefreshToken;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenResponse {

  private String id;

  private String memberId;

  private String tokenValue;

  private AuthProvider authProvider;

  @Builder
  public RefreshTokenResponse(String id, String memberId, String tokenValue, AuthProvider authProvider) {
    this.id = id;
    this.memberId = memberId;
    this.tokenValue = tokenValue;
    this.authProvider = authProvider;
  }

  public static RefreshTokenResponse from(RefreshToken refreshToken) {
    return RefreshTokenResponse.builder()
        .id(refreshToken.getId())
        .memberId(refreshToken.getMember().getId())
        .tokenValue(refreshToken.getTokenValue())
        .authProvider(refreshToken.getAuthProvider())
        .build();
  }
}
