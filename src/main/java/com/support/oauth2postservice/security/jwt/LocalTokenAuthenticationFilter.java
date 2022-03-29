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
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class LocalTokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(UriConstants.SHOULD_NOT_FILTER_URL_PATTERN)
        .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, request.getRequestURI()));
  }

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

    CookieUtils.addLocalTokenToBrowser(response, tokenResponse);
    SecurityUtils.setAuthentication(authentication);
  }
}