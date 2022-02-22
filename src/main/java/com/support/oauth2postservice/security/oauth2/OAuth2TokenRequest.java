package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.ColumnConstants;
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

  private String code;

  @JsonProperty(value = ColumnConstants.Name.REDIRECT_URI)
  private String redirectUri;

  @JsonProperty(value = ColumnConstants.Name.GRANT_TYPE)
  private final String grantType = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();

  @Builder
  public OAuth2TokenRequest(String code, String redirectUri) {
    this.code = code;
    this.redirectUri = redirectUri;
  }

  public MultiValueMap<String, String> toFormData() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add(ColumnConstants.Name.CODE, this.code);
    formData.add(ColumnConstants.Name.GRANT_TYPE, this.grantType);
    formData.add(ColumnConstants.Name.REDIRECT_URI, this.redirectUri);
    return formData;
  }
}
