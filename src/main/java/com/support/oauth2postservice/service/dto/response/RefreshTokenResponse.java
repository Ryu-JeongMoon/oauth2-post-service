package com.support.oauth2postservice.service.dto.response;

import com.support.oauth2postservice.domain.entity.RefreshToken;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenResponse {

  private String id;

  private String memberId;

  private String tokenValue;

  @Builder
  public RefreshTokenResponse(String id, String memberId, String tokenValue) {
    this.id = id;
    this.memberId = memberId;
    this.tokenValue = tokenValue;
  }

  public static RefreshTokenResponse from(RefreshToken refreshToken) {
    return RefreshTokenResponse.builder()
        .id(refreshToken.getId())
        .memberId(refreshToken.getMember().getId())
        .tokenValue(refreshToken.getTokenValue())
        .build();
  }
}
