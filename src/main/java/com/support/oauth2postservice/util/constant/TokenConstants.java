package com.support.oauth2postservice.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenConstants {

  public static final String TYPE = "token-type";
  public static final String ACCESS_ONLY = "access-only";
  public static final String REFRESH_ONLY = "refresh-only";
  public static final String BEARER_TYPE = "Bearer ";
  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String ACCESS_TOKEN = "access-token";
  public static final String REFRESH_TOKEN = "refresh-token";

  public static final String AUTHORITIES = "authorities";

}
