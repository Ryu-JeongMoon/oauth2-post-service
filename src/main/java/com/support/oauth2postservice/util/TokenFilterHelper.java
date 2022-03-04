package com.support.oauth2postservice.util;

import com.support.oauth2postservice.security.jwt.OAuth2TokenVerifier;
import com.support.oauth2postservice.security.jwt.TokenFactory;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenFilterHelper {

  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;
  private final OAuth2TokenVerifier oAuth2TokenVerifier;

  public boolean validateByLocal(String accessToken) {
    return tokenVerifier.isValid(accessToken);
  }

  public boolean validateByOAuth2(String accessToken) {
    return oAuth2TokenVerifier.isValid(accessToken);
  }

  private void validateByVerifier(String token, String refreshToken, TokenVerifier verifier) {
    if (!verifier.isValid(token) && verifier.isValid(refreshToken)) {
      Authentication authentication = verifier.getAuthentication(token);
      TokenResponse tokenResponse = tokenFactory.create(authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }
}
