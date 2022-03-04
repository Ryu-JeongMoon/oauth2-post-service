package com.support.oauth2postservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriUtils {

  private static final String REGISTRATION_ID = "{registrationId}";

  public static String toGoogle(String oldUri) {
    return StringUtils.replace(oldUri, REGISTRATION_ID, "google");
  }
}
