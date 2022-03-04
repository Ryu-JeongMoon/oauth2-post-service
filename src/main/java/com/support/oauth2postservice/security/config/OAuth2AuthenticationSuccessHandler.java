package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.info("authentication = {}", authentication);

    OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
    OAuth2TokenResponse oAuth2TokenResponse = OAuth2TokenResponse.builder()
        .accessToken(principal.getOAuth2Token().getTokenValue())
        .oidcIdToken(principal.getIdToken().getTokenValue())
        .build();

    CookieUtils.setTokenToBrowser(response, oAuth2TokenResponse);
    request.getRequestDispatcher(UriConstants.Mapping.ROOT).forward(request, response);
  }
}