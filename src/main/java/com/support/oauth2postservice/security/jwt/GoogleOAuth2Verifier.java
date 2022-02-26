package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Verifier implements OAuth2TokenVerifier {

  private final WebClientWrappable webClientWrappable;

  @Override
  public boolean isValid(String token) {
    return webClientWrappable.validateByOAuth2(token);
  }

  @Override
  public Authentication getAuthentication(String accessToken) {
    return null;
  }
}
