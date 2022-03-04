package com.support.oauth2postservice.service;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.UriConstants;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2TokenService {

  private final WebClientWrappable webClientWrappable;
  private final ClientRegistrationRepository clientRegistrationRepository;

  public boolean validate(String idToken) {
    return webClientWrappable.validateByOidc(idToken);
  }

  public OAuth2TokenResponse getOAuth2Token(String registrationId, String code) {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

    OAuth2TokenRequest oAuth2TokenRequest = OAuth2TokenRequest.builder()
        .code(code)
        .clientId(clientRegistration.getClientId())
        .clientSecret(clientRegistration.getClientSecret())
        .redirectUri(UriConstants.Full.TOKEN_CALLBACK_URI)
        .grantType(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())
        .build();

    return webClientWrappable.getToken(oAuth2TokenRequest);
  }

  public OAuth2TokenResponse renew(String registrationId, String refreshToken) {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

    OAuth2TokenRequest oAuth2TokenRequest = OAuth2TokenRequest.builder()
        .clientId(clientRegistration.getClientId())
        .clientSecret(clientRegistration.getClientSecret())
        .grantType(AuthorizationGrantType.REFRESH_TOKEN.getValue())
        .refreshToken(refreshToken)
        .build();

    return webClientWrappable.getRenewedToken(oAuth2TokenRequest);
  }
}
