package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenFilterHelper;
import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
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
public class LocalTokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenFilterHelper tokenFilterHelper;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (!isLocalToken(accessToken)) {
      fc.doFilter(req, resp);
      return;
    }

    boolean isValidAccessToken = tokenFilterHelper.validateByLocal(accessToken);
    fc.doFilter(req, resp);
  }

  private boolean isLocalToken(String accessToken) {
    return StringUtils.startsWithIgnoreCase(accessToken, TokenConstants.LOCAL_TOKEN_PREFIX);
  }
}