package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2TokenAuthenticationFilter extends OncePerRequestFilter {

  private final OAuth2TokenVerifier oAuth2TokenVerifier;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!isOAuth2Token(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    boolean isRedirected = redirectIfExpired(req, resp);
    if (isRedirected)
      return;

    fc.doFilter(req, resp);
  }

  private boolean isOAuth2Token(String accessToken) {
    return StringUtils.startsWithIgnoreCase(accessToken, TokenConstants.OAUTH2_ACCESS_TOKEN_PREFIX);
  }

  private boolean redirectIfExpired(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (oAuth2TokenVerifier.isValid(idToken))
      return false;

    String refreshToken = TokenUtils.resolveRefreshToken(request);
    request.getRequestDispatcher(
        UriConstants.Mapping.RENEW_OAUTH2_TOKEN_AND_REDIRECT.replace("{registrationId}", "google") +
            "?redirect_uri=" + request.getRequestURI() + "&refresh_token=" + refreshToken
    ).forward(request, response);
    return true;
  }
}