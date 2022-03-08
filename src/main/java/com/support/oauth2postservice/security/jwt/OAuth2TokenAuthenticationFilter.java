package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (!oAuth2TokenVerifier.isGoogleToken(idToken)) {
      chain.doFilter(request, response);
      return;
    }

    setAuthenticationIfValid(idToken);

    boolean isForwarded = forwardIfExpired(request, response);
    if (isForwarded)
      return;

    chain.doFilter(request, response);
  }

  private void setAuthenticationIfValid(String idToken) {
    if (oAuth2TokenVerifier.isValid(idToken))
      SecurityUtils.setAuthentication(oAuth2TokenVerifier.getAuthentication(idToken));
  }

  private boolean forwardIfExpired(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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