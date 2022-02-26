package com.support.oauth2postservice.util;

import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TokenUtils {

  private static final String EMPTY_VALUE = "";

  public static String resolveAccessToken(HttpServletRequest request) {
    String token = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);
    return StringUtils.startsWithIgnoreCase(token, TokenConstants.BEARER_TYPE) ?
        token.substring(TokenConstants.BEARER_TYPE.length()) : "";
  }

  public static String resolveRefreshToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.REFRESH_TOKEN)
        .map(Cookie::getValue)
        .orElseGet(() -> EMPTY_VALUE);
  }
}
