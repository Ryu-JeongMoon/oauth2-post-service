package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.service.RefreshTokenService;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.TokenUtils;
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
  private final RefreshTokenService refreshTokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (!oAuth2TokenVerifier.isGoogleToken(idToken)) {
      chain.doFilter(request, response);
      return;
    }

    setAuthenticationIfValid(idToken);

    forwardIfExpired(request, response);

    chain.doFilter(request, response);
  }

  private void setAuthenticationIfValid(String idToken) {
    if (oAuth2TokenVerifier.isValid(idToken))
      SecurityUtils.setAuthentication(oAuth2TokenVerifier.getAuthentication(idToken));
  }

  private void forwardIfExpired(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String idToken = TokenUtils.resolveIdToken(request);
    if (oAuth2TokenVerifier.isValid(idToken))
      return;

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