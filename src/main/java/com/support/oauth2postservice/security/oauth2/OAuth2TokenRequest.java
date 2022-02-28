package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenRequest {

  @JsonProperty(value = TokenConstants.GRANT_TYPE)
  private final String grantType = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
  private String code;
  @JsonProperty(value = TokenConstants.REDIRECT_URI)
  private String redirectUri;

  @Builder
  public OAuth2TokenRequest(String code, String redirectUri) {
    this.code = code;
    this.redirectUri = redirectUri;
  }

  public MultiValueMap<String, String> toFormData() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(TokenConstants.CODE, this.code);
    formData.add(TokenConstants.GRANT_TYPE, this.grantType);
    formData.add(TokenConstants.REDIRECT_URI, this.redirectUri);
    return formData;
  }
}
