package com.support.oauth2postservice.security.jwt;

public interface OAuth2TokenVerifier extends TokenVerifier {

  @Override
  default boolean isLocalToken(String accessToken) {
    return false;
  }

  boolean isGoogleToken(String idToken);
}
