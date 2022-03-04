package com.support.oauth2postservice.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenConstants {

  public static final String BEARER_TYPE = "Bearer ";
  public static final String LOCAL_TOKEN_PREFIX = "eyJraW";
  public static final String OAUTH2_TOKEN_PREFIX = "ya29";
  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String CODE = "code";
  public static final String ERROR = "error";
  public static final String SCOPE = "scope";
  public static final String CLAIMS = "claims";
  public static final String GRANT_TYPE = "grant_type";
  public static final String TOKEN_TYPE = "token_type";
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String EXPIRES_IN = "expires_in";
  public static final String REDIRECT_URI = "redirect_uri";

  public static final String ID_TOKEN = "id_token";
  public static final String ACCESS_TOKEN = "access_token";
  public static final String REFRESH_TOKEN = "refresh_token";

  public static final String EMAIL = "email";
  public static final String USER_ID = "user_id";
  public static final String AUTHORITIES = "authorities";
  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
}
