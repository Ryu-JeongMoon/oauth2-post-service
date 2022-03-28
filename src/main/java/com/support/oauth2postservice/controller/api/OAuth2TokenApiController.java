package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.service.OAuth2TokenService;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2TokenApiController {

  private final OAuth2TokenService oAuth2TokenService;

  @GetMapping(UriConstants.Mapping.VALIDATE_OAUTH2_TOKEN)
  @Operation(summary = "토큰 검증", description = "구글 ID TOKEN 검증, 만료 시간 1시간\nboolean 값 리턴")
  public boolean validate(@RequestParam(TokenConstants.ID_TOKEN) String idToken) {
    return oAuth2TokenService.validate(idToken);
  }

  @GetMapping(UriConstants.Mapping.RENEW_OAUTH2_TOKEN)
  @Operation(summary = "토큰 갱신", description = "기존의 REFRESH TOKEN 전송해 새로운 ID, ACCESS TOKEN 발급")
  public OAuth2TokenResponse renew(
      @PathVariable String registrationId,
      @RequestParam(TokenConstants.REFRESH_TOKEN) String refreshToken) {

    return oAuth2TokenService.renew(registrationId, refreshToken);
  }
}