package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.service.OAuth2TokenService;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2TokenApiController {

  private final OAuth2TokenService oAuth2TokenService;

  @GetMapping(UriConstants.Mapping.ISSUE_OAUTH2_TOKEN)
  public OAuth2TokenResponse getOAuth2Token(@PathVariable String registrationId, @RequestParam String code) {
    return oAuth2TokenService.getOAuth2Token(registrationId, code);
  }

  @GetMapping(UriConstants.Mapping.VALIDATE_OAUTH2_TOKEN)
  public boolean validate(@RequestParam(TokenConstants.ID_TOKEN) String idToken) {
    return oAuth2TokenService.validate(idToken);
  }

  @GetMapping(UriConstants.Mapping.RENEW_OAUTH2_TOKEN)
  public OAuth2TokenResponse renew(
      @PathVariable String registrationId,
      @RequestParam(TokenConstants.REFRESH_TOKEN) String refreshToken) {

    return oAuth2TokenService.renew(registrationId, refreshToken);
  }
}