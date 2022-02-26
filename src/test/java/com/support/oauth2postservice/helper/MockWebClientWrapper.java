package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

@Component
public class MockWebClientWrapper implements WebClientWrappable {

  public static final String SCOPE = "panda";
  public static final String TOKEN_TYPE = "bear";
  public static final String EXPIRES_IN = "rabbit";
  public static final String ACCESS_TOKEN = "mouse";
  public static final String REFRESH_TOKEN = "tiger";

  @Override
  public OAuth2TokenResponse getOAuth2TokenResponse(ClientRegistration clientRegistration, OAuth2TokenRequest oAuth2TokenRequest) {
    return new OAuth2TokenResponse(SCOPE, TOKEN_TYPE, EXPIRES_IN, ACCESS_TOKEN, REFRESH_TOKEN);
  }

  @Override
  public boolean validateByOAuth2(String token) {
    return false;
  }
}
