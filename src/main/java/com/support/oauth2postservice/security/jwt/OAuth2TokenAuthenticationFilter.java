package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenFilterHelper;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
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
public class OAuth2TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFilterHelper tokenFilterHelper;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!isOAuth2Token(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    boolean isValidAccessToken = tokenFilterHelper.validateByOAuth2(accessToken);
    if (!isValidAccessToken) {
      String refreshToken = TokenUtils.resolveRefreshToken(req);
      String pathInfo = req.getPathInfo();
      req.getRequestDispatcher(
          UriConstants.Mapping.RENEW_GOOGLE_TOKEN + "?redirect_uri=" + pathInfo + "&refresh_token=" + refreshToken
      ).forward(req, resp);
    }

    fc.doFilter(req, resp);
  }

  private boolean isOAuth2Token(String accessToken) {
    return StringUtils.startsWithIgnoreCase(accessToken, TokenConstants.OAUTH2_TOKEN_PREFIX);
  }
}