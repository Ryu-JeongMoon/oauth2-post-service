package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenResponse {

  private String scope;

  @JsonProperty(TokenConstants.TOKEN_TYPE)
  private String tokenType;

  @JsonProperty(TokenConstants.EXPIRES_IN)
  private String expiresIn;

  @JsonProperty(TokenConstants.ACCESS_TOKEN)
  private String accessToken;

  @JsonProperty(TokenConstants.REFRESH_TOKEN)
  private String refreshToken;

  @JsonProperty(TokenConstants.ID_TOKEN)
  private String oidcIdToken;

  @Builder
  public OAuth2TokenResponse(String scope, String tokenType, String expiresIn, String accessToken, String refreshToken, String oidcIdToken) {
    this.scope = scope;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.oidcIdToken = oidcIdToken;
  }

  public static OAuth2TokenResponse empty() {
    return new OAuth2TokenResponse();
  }
}
