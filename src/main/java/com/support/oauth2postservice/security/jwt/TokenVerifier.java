package com.support.oauth2postservice.security.jwt;

import org.springframework.security.core.Authentication;

public interface TokenVerifier {

  boolean isValid(String token);

  Authentication getAuthentication(String accessToken);
}
