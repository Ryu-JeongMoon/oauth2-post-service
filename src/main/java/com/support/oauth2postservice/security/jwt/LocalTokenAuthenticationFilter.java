package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LocalTokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!isLocalToken(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    boolean isReissued = reissueIfExpired(req, resp);
    if (isReissued)
      return;

    fc.doFilter(req, resp);
  }

  private boolean isLocalToken(String accessToken) {
    return StringUtils.startsWithIgnoreCase(accessToken, TokenConstants.LOCAL_ACCESS_TOKEN_PREFIX);
  }

  private boolean reissueIfExpired(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = TokenUtils.resolveAccessToken(request);
    if (tokenVerifier.isValid(accessToken))
      return false;

    String refreshToken = TokenUtils.resolveRefreshToken(request);
    if (!tokenVerifier.isValid(refreshToken))
      return false;

    Authentication authentication = tokenVerifier.getAuthentication(accessToken);
    TokenResponse tokenResponse = tokenFactory.create(authentication);

    CookieUtils.setLocalTokenToBrowser(response, tokenResponse);
    SecurityUtils.setAuthentication(authentication);
    return true;
  }
}