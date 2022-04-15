package com.support.oauth2postservice.service;

import com.support.oauth2postservice.security.jwt.TokenFactory;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalTokenService {

  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;

  public TokenResponse renew(String refreshToken) {
    Authentication authentication = tokenVerifier.getAuthentication(refreshToken);
    boolean isValid = tokenVerifier.isValid(refreshToken);

    return isValid
        ? tokenFactory.renew(authentication, refreshToken)
        : tokenFactory.create(authentication);
  }
}
