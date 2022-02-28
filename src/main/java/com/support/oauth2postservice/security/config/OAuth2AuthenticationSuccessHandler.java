package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
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
    OidcIdToken idToken = principal.getIdToken();

    response.addHeader(TokenConstants.AUTHORIZATION_HEADER, TokenConstants.BEARER_TYPE + idToken);

    String registrationId = request.getRequestURI().substring(UriConstants.Mapping.DEFAULT_REDIRECT_URL_PREFIX.length());

    request.setAttribute("code", request.getParameter("code"));
    request.getRequestDispatcher("/").forward(request, response);
  }
}