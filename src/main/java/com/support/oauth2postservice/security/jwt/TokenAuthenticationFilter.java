package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.TokenUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenVerifier tokenVerifier;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    doFilterByLocalToken(req, resp, fc);

    fc.doFilter(req, resp);
  }

  private void doFilterByLocalToken(HttpServletRequest req, HttpServletResponse resp, FilterChain fc) throws ServletException, IOException {
    String accessToken = TokenUtils.resolveAccessToken(req);
    if (tokenVerifier.isValid(accessToken)) {
      Authentication authentication = tokenVerifier.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      fc.doFilter(req, resp);
    }

    String refreshToken = TokenUtils.resolveRefreshToken(req);
    if (tokenVerifier.isValid(refreshToken)) {
      req.setAttribute(TokenConstants.REFRESH_TOKEN, refreshToken);
      req.getRequestDispatcher(UriConstants.Mapping.ACCESS_TOKEN_REISSUE).forward(req, resp);
    }
  }
}
