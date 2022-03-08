package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
public class LocalTokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!tokenVerifier.isLocalToken(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    setAuthenticationIfValid(accessToken);

    reissueIfExpired(req, resp);

    fc.doFilter(req, resp);
  }

  private void setAuthenticationIfValid(String accessToken) {
    if (tokenVerifier.isValid(accessToken))
      SecurityUtils.setAuthentication(tokenVerifier.getAuthentication(accessToken));
  }

  private void reissueIfExpired(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = TokenUtils.resolveAccessToken(request);
    if (tokenVerifier.isValid(accessToken))
      return;

    String refreshToken = TokenUtils.resolveRefreshToken(request);
    if (!tokenVerifier.isValid(refreshToken))
      return;

    Authentication authentication = tokenVerifier.getAuthentication(accessToken);
    TokenResponse tokenResponse = tokenFactory.create(authentication);

    CookieUtils.setLocalTokenToBrowser(response, tokenResponse);
    SecurityUtils.setAuthentication(authentication);
  }
}