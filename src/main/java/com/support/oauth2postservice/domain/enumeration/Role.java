package com.support.oauth2postservice.domain.enumeration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role implements GrantedAuthority {

  USER("ROLE_USER"),
  MANAGER("ROLE_MANAGER"),
  ADMIN("ROLE_ADMIN");

  private final String key;

  public static Role valueOfCaseInsensitively(String name) {
    return valueOf(StringUtils.upperCase(name));
  }

  @Override
  public String getAuthority() {
    return key;
  }
}
