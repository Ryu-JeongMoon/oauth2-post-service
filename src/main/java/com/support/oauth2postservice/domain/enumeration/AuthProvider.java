package com.support.oauth2postservice.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

public enum AuthProvider {
  LOCAL,
  GOOGLE;

  public static AuthProvider caseInsensitiveValueOf(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}
