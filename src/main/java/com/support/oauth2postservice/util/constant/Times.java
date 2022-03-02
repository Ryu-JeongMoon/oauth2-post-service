package com.support.oauth2postservice.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Times {

  ACCESS_TOKEN_EXPIRATION_SECONDS(60 * 30),
  REFRESH_TOKEN_EXPIRATION_SECONDS(60 * 60 * 24 * 7),

  ACCESS_TOKEN_EXPIRATION_MILLIS(1000 * 60 * 30),
  REFRESH_TOKEN_EXPIRATION_MILLIS(1000 * 60 * 60 * 24 * 7);

  private final long number;

  public static Times toEnum(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}
