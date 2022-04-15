package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.OAuth2TokenVerifier;
import com.support.oauth2postservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.service.OAuth2TokenService;
import com.support.oauth2postservice.service.RefreshTokenService;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class OAuth2TokenViewController {

  private final OAuth2TokenService oAuth2TokenService;
  private final OAuth2TokenVerifier oAuth2TokenVerifier;
  private final RefreshTokenService refreshTokenService;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @GetMapping(UriConstants.Mapping.ISSUE_OAUTH2_TOKEN)
  public String loginIfRefreshTokenNotExists(
      @PathVariable String registrationId,
      @RequestParam String code,
      HttpServletRequest request,
      HttpServletResponse response) {

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    OAuth2TokenResponse oAuth2TokenResponse = oAuth2TokenService.getOAuth2Token(registrationId, code);
    String oidcIdToken = oAuth2TokenResponse.getOidcIdToken();

    Authentication authentication = oAuth2TokenVerifier.getAuthentication(oidcIdToken);
    OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
    refreshTokenService.saveOrUpdate(principal, oAuth2TokenResponse.getRefreshToken());

    SecurityUtils.setAuthentication(authentication);
    CookieUtils.addOAuth2TokenToBrowser(response, oAuth2TokenResponse);
    return getUriByRole(principal);
  }

  private String getUriByRole(UserPrincipal principal) {
    return isUser(principal)
        ? UriConstants.Keyword.REDIRECT + UriConstants.Mapping.POSTS
        : UriConstants.Keyword.REDIRECT + UriConstants.Mapping.MEMBERS;
  }

  private boolean isUser(UserPrincipal principal) {
    return principal.getAuthorities()
        .stream()
        .anyMatch(Role.USER::equals);
  }

  @GetMapping(UriConstants.Mapping.RENEW_OAUTH2_TOKEN_AND_REDIRECT)
  public String renewAndRedirect(
      @PathVariable String registrationId,
      @RequestParam(TokenConstants.REFRESH_TOKEN) String refreshToken,
      @RequestParam(value = TokenConstants.REDIRECT_URI, defaultValue = UriConstants.Mapping.ROOT) String redirectUri,
      HttpServletResponse response) {

    OAuth2TokenResponse renewedTokenResponse = oAuth2TokenService.renew(registrationId, refreshToken);

    String oidcIdToken = renewedTokenResponse.getOidcIdToken();
    Authentication authentication = oAuth2TokenVerifier.getAuthentication(oidcIdToken);
    SecurityUtils.setAuthentication(authentication);

    CookieUtils.addOAuth2TokenToBrowser(response, renewedTokenResponse);
    return UriConstants.Keyword.REDIRECT + redirectUri;
  }
}
