package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private static final String LOCAL_KEY_PREFIX = "eyJraW";

  private final TokenVerifier tokenVerifier;
  private final OAuth2TokenVerifier oAuth2TokenVerifier;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!StringUtils.hasText(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    if (!accessToken.startsWith(LOCAL_KEY_PREFIX))
      doFilterByOAuth2Token(accessToken);

    doFilterByLocalToken(accessToken);
    fc.doFilter(req, resp);
  }

  private void doFilterByLocalToken(String token) throws ServletException, IOException {
    doFilterByVerifier(token, tokenVerifier);
  }

  private void doFilterByOAuth2Token(String token) throws ServletException, IOException {
    doFilterByVerifier(token, oAuth2TokenVerifier);
  }

  private void doFilterByVerifier(String token, TokenVerifier verifier) throws IOException, ServletException {
    if (verifier.isValid(token)) {
      Authentication authentication = verifier.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }
}