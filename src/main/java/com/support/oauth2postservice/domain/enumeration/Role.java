package com.support.oauth2postservice.domain.enumeration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role implements GrantedAuthority {

  USER("ROLE_USER", 0),
  MANAGER("ROLE_MANAGER", 1),
  ADMIN("ROLE_ADMIN", 2);

  private final String key;
  private final int level;

  public static Role caseInsensitiveValueOf(String name) {
    return valueOf(StringUtils.upperCase(name));
  }

  public boolean isSuperiorThan(Role role) {
    if (role == null)
      return true;

    return this.level >= role.level;
  }

  public boolean isInferiorThan(Role role) {
    if (role == null)
      return false;

    return this.level < role.level;
  }

  @Override
  public String getAuthority() {
    return key;
  }
}
