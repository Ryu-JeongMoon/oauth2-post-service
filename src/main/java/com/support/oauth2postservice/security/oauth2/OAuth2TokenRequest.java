package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenRequest {

  @JsonProperty(TokenConstants.GRANT_TYPE)
  private final String grantType = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
  private String code;
  @JsonProperty(TokenConstants.CLIENT_ID)
  private String clientId;
  @JsonProperty(TokenConstants.CLIENT_SECRET)
  private String clientSecret;
  @JsonProperty(TokenConstants.REDIRECT_URI)
  private String redirectUri;

  @Builder
  public OAuth2TokenRequest(String code, String clientId, String clientSecret, String redirectUri) {
    this.code = code;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
  }

  public MultiValueMap<String, String> toFormData() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(TokenConstants.CODE, this.code);
    formData.add(TokenConstants.GRANT_TYPE, this.grantType);
    formData.add(TokenConstants.REDIRECT_URI, this.redirectUri);
    formData.add(TokenConstants.CLIENT_ID, this.clientId);
    formData.add(TokenConstants.CLIENT_SECRET, this.clientSecret);
    return formData;
  }
}
