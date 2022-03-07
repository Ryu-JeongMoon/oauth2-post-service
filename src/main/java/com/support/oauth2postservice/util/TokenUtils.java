package com.support.oauth2postservice.util;

import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

  private static final String EMPTY_VALUE = "";

  public static String resolveAccessToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.ACCESS_TOKEN)
        .filter(cookie -> cookie.getValue().startsWith(TokenConstants.BEARER_TYPE))
        .map(cookie -> cookie.getValue().substring(TokenConstants.BEARER_TYPE.length()))
        .orElseGet(() -> EMPTY_VALUE);
  }

  public static String resolveIdToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.ID_TOKEN)
        .map(Cookie::getValue)
        .orElseGet(() -> EMPTY_VALUE);
  }

  public static String resolveRefreshToken(HttpServletRequest request) {
    return CookieUtils.getCookie(request, TokenConstants.REFRESH_TOKEN)
        .map(Cookie::getValue)
        .orElseGet(() -> EMPTY_VALUE);
  }
}
