package com.support.oauth2postservice.security.oauth2;

import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.ConstructorProperties;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenRequest {

  private String code;
  private String clientId;
  private String clientSecret;
  private String redirectUri;
  private String grantType;
  private String refreshToken;

  @Builder
  @ConstructorProperties({
      TokenConstants.CODE, TokenConstants.CLIENT_ID, TokenConstants.CLIENT_SECRET,
      TokenConstants.REDIRECT_URI, TokenConstants.GRANT_TYPE, TokenConstants.REFRESH_TOKEN
  })
  public OAuth2TokenRequest(String code, String clientId, String clientSecret, String redirectUri, String grantType, String refreshToken) {
    this.code = code;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
    this.grantType = grantType;
    this.refreshToken = refreshToken;
  }

  public MultiValueMap<String, String> toFormData() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(TokenConstants.CODE, this.code);
    formData.add(TokenConstants.GRANT_TYPE, this.grantType);
    formData.add(TokenConstants.REDIRECT_URI, this.redirectUri);
    formData.add(TokenConstants.CLIENT_ID, this.clientId);
    formData.add(TokenConstants.CLIENT_SECRET, this.clientSecret);
    formData.add(TokenConstants.REFRESH_TOKEN, this.refreshToken);
    return formData;
  }
}
