package com.support.oauth2postservice.util;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.Times;
import com.support.oauth2postservice.util.constant.TokenConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

  public static void setTokenToBrowser(HttpServletResponse response, OAuth2TokenResponse renewedTokenResponse) {
    String accessToken = renewedTokenResponse.getAccessToken();
    if (StringUtils.isNotBlank(accessToken)) {
      CookieUtils.addCookie(
          response,
          TokenConstants.ACCESS_TOKEN,
          TokenConstants.BEARER_TYPE + accessToken,
          Times.ACCESS_TOKEN_EXPIRATION_SECONDS.getValue());
    }

    String oidcIdToken = renewedTokenResponse.getOidcIdToken();
    if (StringUtils.isNotBlank(oidcIdToken)) {
      CookieUtils.addCookie(
          response,
          TokenConstants.ID_TOKEN,
          oidcIdToken,
          Times.ID_TOKEN_EXPIRATION_SECONDS.getValue());
    }
  }

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0)
      return Optional.empty();

    return Arrays.stream(cookies)
        .filter(cookie -> StringUtils.equals(cookie.getName(), name))
        .findAny();
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0)
      return;

    Arrays.stream(cookies)
        .filter(cookie -> StringUtils.equals(cookie.getName(), name))
        .forEach(cookie -> {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        });
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder()
        .encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
    return clazz.cast(SerializationUtils.deserialize(
        Base64.getUrlDecoder().decode(cookie.getValue())));
  }
}
