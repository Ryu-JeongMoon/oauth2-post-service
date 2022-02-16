package com.support.oauth2postservice.security.jwt;

public interface TokenVerifier {

  boolean isValid(String token);
}
