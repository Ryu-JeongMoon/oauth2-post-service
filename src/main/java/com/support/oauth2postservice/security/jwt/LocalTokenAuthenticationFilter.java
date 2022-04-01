package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return UriConstants.SHOULD_NOT_FILTER_URL_PATTERN
        .stream()
        .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, request.getRequestURI()));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!tokenVerifier.isLocalToken(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    if (!setAuthenticationIfValid(accessToken))
      reissueIfExpired(req, resp);

    fc.doFilter(req, resp);
  }

  private boolean setAuthenticationIfValid(String accessToken) {
    boolean isValid = tokenVerifier.isValid(accessToken);
    if (isValid)
      SecurityUtils.setAuthentication(tokenVerifier.getAuthentication(accessToken));

    return isValid;
  }

  private void reissueIfExpired(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = TokenUtils.resolveRefreshToken(request);
    if (!tokenVerifier.isValid(refreshToken))
      return;

    String accessToken = TokenUtils.resolveAccessToken(request);
    Authentication authentication = tokenVerifier.getAuthentication(accessToken);
    TokenResponse tokenResponse = tokenFactory.create(authentication);

    CookieUtils.addLocalTokenToBrowser(response, tokenResponse);
    SecurityUtils.setAuthentication(authentication);
  }
}