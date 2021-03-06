package com.support.oauth2postservice.util.wrapper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;

public interface WebClientWrappable {

  boolean validateByOidc(String idToken);

  OAuth2TokenResponse getToken(OAuth2TokenRequest oAuth2TokenRequest);

  OAuth2TokenResponse getRenewedToken(OAuth2TokenRequest oAuth2TokenRequest);
}
