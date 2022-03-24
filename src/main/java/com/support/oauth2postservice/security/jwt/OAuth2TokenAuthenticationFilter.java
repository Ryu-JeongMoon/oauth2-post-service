package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.service.RefreshTokenService;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2TokenAuthenticationFilter extends OncePerRequestFilter {

  private final OAuth2TokenVerifier oAuth2TokenVerifier;
  private final RefreshTokenService refreshTokenService;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return StringUtils.startsWithAny(request.getRequestURI(), UriConstants.SHOULD_NOT_FILTER_URL_PREFIX);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (!oAuth2TokenVerifier.isGoogleToken(idToken)) {
      chain.doFilter(request, response);
      return;
    }

    if (!setAuthenticationIfValid(idToken))
      forwardIfExpired(request, response);

    chain.doFilter(request, response);
  }

  private boolean setAuthenticationIfValid(String idToken) {
    boolean isValid = oAuth2TokenVerifier.isValid(idToken);
    if (isValid)
      SecurityUtils.setAuthentication(oAuth2TokenVerifier.getAuthentication(idToken));

    return isValid;
  }

  private void forwardIfExpired(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String idToken = TokenUtils.resolveIdToken(request);
    String refreshToken = getRefreshTokenFromIdToken(idToken);
    String targetUri = String.format(
        "/oauth2/google/renewal/redirect?redirect_uri=%s&refresh_token=%s", request.getRequestURI(), refreshToken);
    request.getRequestDispatcher(targetUri).forward(request, response);
  }

  private String getRefreshTokenFromIdToken(String idToken) {
    OAuth2UserPrincipal principal = (OAuth2UserPrincipal) oAuth2TokenVerifier
        .getAuthentication(idToken)
        .getPrincipal();

    RefreshTokenResponse refreshTokenResponse = refreshTokenService.findByEmail(principal.getEmail());
    return refreshTokenResponse.getTokenValue();
  }
}