package com.support.oauth2postservice.util;

import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import com.support.oauth2postservice.service.RefreshTokenService;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenFilterHelper {

  private final RefreshTokenService refreshTokenService;

  public String getRefreshTokenFromIdToken(TokenVerifier tokenVerifier, String idToken) {
    UserPrincipal principal = (UserPrincipal) tokenVerifier
        .getAuthentication(idToken)
        .getPrincipal();

    RefreshTokenResponse refreshTokenResponse = refreshTokenService.findByEmail(principal.getEmail());
    return refreshTokenResponse.getTokenValue();
  }

  public boolean setAuthenticationIfValid(TokenVerifier tokenVerifier, String idToken) {
    boolean isValid = tokenVerifier.isValid(idToken);
    if (isValid)
      SecurityUtils.setAuthentication(tokenVerifier.getAuthentication(idToken));

    return isValid;
  }
}
