package com.support.oauth2postservice.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

public enum Status {
  ACTIVE,
  INACTIVE;

  public static Status toEnum(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}
