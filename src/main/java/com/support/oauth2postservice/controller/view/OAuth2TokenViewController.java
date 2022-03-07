package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.service.OAuth2TokenService;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class OAuth2TokenViewController {

  private final OAuth2TokenService oAuth2TokenService;

  @GetMapping(UriConstants.Mapping.RENEW_OAUTH2_TOKEN_AND_REDIRECT)
  public String renewAndRedirect(
      @PathVariable String registrationId,
      @RequestParam(TokenConstants.REFRESH_TOKEN) String refreshToken,
      @RequestParam(value = TokenConstants.REDIRECT_URI, defaultValue = UriConstants.Mapping.ROOT) String redirectUri,
      HttpServletResponse response) {

    OAuth2TokenResponse renewedTokenResponse = oAuth2TokenService.renew(registrationId, refreshToken);

    CookieUtils.setOAuth2TokenToBrowser(response, renewedTokenResponse);
    return UriConstants.Keyword.REDIRECT + redirectUri;
  }
}
