package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenFilterHelper;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFilterHelper tokenFilterHelper;
  private final OAuth2TokenVerifier oAuth2TokenVerifier;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return PatternMatchUtils.simpleMatch(UriConstants.SHOULD_NOT_FILTER_URL_PATTERNS, request.getRequestURI());
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (!oAuth2TokenVerifier.isGoogleToken(idToken)) {
      chain.doFilter(request, response);
      return;
    }

    if (!tokenFilterHelper.setAuthenticationIfValid(oAuth2TokenVerifier, idToken))
      forwardToRenew(request, response);

    chain.doFilter(request, response);
  }

  private void forwardToRenew(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String idToken = TokenUtils.resolveIdToken(request);
    String refreshToken = tokenFilterHelper.getRefreshTokenFromIdToken(oAuth2TokenVerifier, idToken);
    String targetUri = String.format(
        "/oauth2/google/renewal/redirect?redirect_uri=%s&refresh_token=%s", request.getRequestURI(), refreshToken);
    request.getRequestDispatcher(targetUri).forward(request, response);
  }
}