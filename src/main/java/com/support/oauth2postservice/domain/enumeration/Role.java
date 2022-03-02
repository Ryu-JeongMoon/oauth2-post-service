package com.support.oauth2postservice.domain.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role {

  USER("ROLE_USER"),
  MANAGER("ROLE_MANAGER"),
  ADMIN("ROLE_ADMIN");

  private final String key;

  public static Role toEnum(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}
