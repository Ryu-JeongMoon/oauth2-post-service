package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class OAuth2TokenResponse {

  private String scope;

  @JsonProperty(value = ColumnConstants.Name.TOKEN_TYPE)
  private String tokenType;

  @JsonProperty(value = ColumnConstants.Name.EXPIRES_IN)
  private String expiresIn;

  @JsonProperty(value = ColumnConstants.Name.ACCESS_TOKEN)
  private String accessToken;

  @JsonProperty(value = ColumnConstants.Name.REFRESH_TOKEN)
  private String refreshToken;

  public OAuth2TokenResponse(String scope, String tokenType, String expiresIn, String accessToken, String refreshToken) {
    this.scope = scope;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
