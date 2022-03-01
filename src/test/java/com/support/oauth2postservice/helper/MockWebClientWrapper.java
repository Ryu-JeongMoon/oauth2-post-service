package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import org.springframework.stereotype.Component;

@Component
public class MockWebClientWrapper implements WebClientWrappable {

  private static final String SCOPE = "panda";
  private static final String TOKEN_TYPE = "bear";
  private static final String EXPIRES_IN = "rabbit";
  private static final String ACCESS_TOKEN = "mouse";
  private static final String REFRESH_TOKEN = "tiger";
  private static final String OIDC_ID_TOKEN = "elephant";

  @Override
  public OAuth2TokenResponse getOAuth2TokenResponse(OAuth2TokenRequest oAuth2TokenRequest) {
    return new OAuth2TokenResponse(SCOPE, TOKEN_TYPE, EXPIRES_IN, ACCESS_TOKEN, REFRESH_TOKEN, OIDC_ID_TOKEN);
  }

  @Override
  public boolean validateByOidc(String idToken) {
    return false;
  }
}
