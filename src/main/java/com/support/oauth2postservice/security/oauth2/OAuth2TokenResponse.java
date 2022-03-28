package com.support.oauth2postservice.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.support.oauth2postservice.util.constant.TokenConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenResponse {

  @Schema(description = "접근 범위", example = "email, profile", required = true)
  private String scope;

  @JsonProperty(TokenConstants.TOKEN_TYPE)
  @Schema(description = "토큰 타입", example = "Bearer", required = true)
  private String tokenType;

  @JsonProperty(TokenConstants.EXPIRES_IN)
  @Schema(description = "유효 시간", example = "102392383", required = true)
  private String expiresIn;

  @JsonProperty(TokenConstants.ACCESS_TOKEN)
  @Schema(description = "ACCESS TOKEN", required = true,
      example = "Bearer ya29.A0ARrdaM8imdWLA1IXuns_93qwXct-k9xIHVvdh1rwu4Nco0VnVBLikyotIz_sP-zvL0V1U2LkTdHBv99jvdA3XDcdnGbau3rug25y3gM4y4QL2NoWL919-33ZPTJqEzQHBE2vH7EsSMoR3u2-uPB5p8lqJUGtAQ")
  private String accessToken;

  @JsonProperty(TokenConstants.REFRESH_TOKEN)
  @Schema(description = "REFRESH TOKEN", required = true,
      example = "1//0ejagQyn8SttPCgYIARAAGA4SNwF-L9IrLLph0s3DO6fy8pIyvIA3csMT3zOwBd7Vu5jNB-NZek41nr6SSddwOt9WbJ27UdjqcCc")
  private String refreshToken;

  @JsonProperty(TokenConstants.ID_TOKEN)
  @Schema(description = "ID TOKEN", required = true,
      example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU4YjQyOTY2MmRiMDc4NmYyZWZlZmUxM2MxZWIxMmEyOGRjNDQyZDAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0NDc2Njg1MDI2OTUtaWxkMXJtNXNiaXZiOWlua3J2dWlkZDg5YWdwbWMyNmcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0NDc2Njg1MDI2OTUtaWxkMXJtNXNiaXZiOWlua3J2dWlkZDg5YWdwbWMyNmcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDIwOTU0NjY5NzYxNjQyNzM2OTYiLCJlbWFpbCI6InJqbTkzMDNAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJUR0JKc3VmSVQ1aG82VEszMmwxbGpnIiwibmFtZSI6Ik1vb24iLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FPaDE0R2hkQlIySm5zeTNUc3JkVTQybFRHYS1nOXhUQ0xITzRiZ3hLVlZ4PXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6Ik1vb24iLCJsb2NhbGUiOiJrbyIsImlhdCI6MTY0ODQ0MDMzMywiZXhwIjoxNjQ4NDQzOTMzfQ.h6jWxylBk0Xt19yR8BvW7CwpPfDOf0QzCDOz3B0QOP-SYfPXOaYce60l0JCw0ZDBkhVZ2ww9MrVK8Ec4px-JCeyH2ROx-FMrFoo7Mqf47I_T01nSAZ90XuAoa6RZxmetwYZbNKrQrx0JYnTLLi-67xpQN5-r8a0cZnZm8VX_WIJ6LHxLj720cjLO2i8czLs9tpxGU0EgzmHkGK5KQNDBnRI8PfjIRduhfYjO02NWzyqUkyyM_jVB91tFVg9Fj3YuvWgWREOkGVu1iP2Pys6GQgun87XcypE-j7b9ZB5WNnFKA14l5abJBb0u65tTZhTx1EMqMAq-SQ-zc42It_rbjQ")
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
