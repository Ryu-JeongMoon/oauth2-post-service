package com.support.oauth2postservice.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Times {

  ID_TOKEN_EXPIRATION_SECONDS(60 * 30),
  ACCESS_TOKEN_EXPIRATION_SECONDS(60 * 30),
  REFRESH_TOKEN_EXPIRATION_SECONDS(60 * 60 * 24 * 7),

  ACCESS_TOKEN_EXPIRATION_MILLIS(1000 * 60 * 30),
  REFRESH_TOKEN_EXPIRATION_MILLIS(1000 * 60 * 60 * 24 * 7),

  COOKIE_EXPIRATION_SECONDS( 60 * 60 * 24 * 365 + 60 * 60 * 9);

  private final int value;

  public static Times valueOfCaseInsensitively(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}
