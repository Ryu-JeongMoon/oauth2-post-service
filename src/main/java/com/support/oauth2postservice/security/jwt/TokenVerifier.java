package com.support.oauth2postservice.security.jwt;

import org.springframework.security.core.Authentication;

public interface TokenVerifier {

  boolean isValid(String token);

  boolean isLocalToken(String accessToken);

  Authentication getAuthentication(String token);
}
