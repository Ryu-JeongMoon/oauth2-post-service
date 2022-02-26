package com.support.oauth2postservice.util.wrapper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface WebClientWrappable {

  OAuth2TokenResponse getOAuth2TokenResponse(ClientRegistration clientRegistration, OAuth2TokenRequest oAuth2TokenRequest);

  boolean validateByOAuth2(String token);
}
