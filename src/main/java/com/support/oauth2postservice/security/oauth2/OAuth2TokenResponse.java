package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class OAuth2TokenResponse {

  private String scope;

  @JsonProperty(value = TokenConstants.TOKEN_TYPE)
  private String tokenType;

  @JsonProperty(value = TokenConstants.EXPIRES_IN)
  private String expiresIn;

  @JsonProperty(value = TokenConstants.ACCESS_TOKEN)
  private String accessToken;

  @JsonProperty(value = TokenConstants.REFRESH_TOKEN)
  private String refreshToken;

  public OAuth2TokenResponse(String scope, String tokenType, String expiresIn, String accessToken, String refreshToken) {
    this.scope = scope;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
