package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.UriConstants;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private final WebClientWrappable webClientWrappable;
  private final ClientRegistrationRepository clientRegistrationRepository;

  @GetMapping(UriConstants.Mapping.OAUTH2_CALLBACK)
  public OAuth2TokenResponse oauth2Callback(@PathVariable String registrationId, @RequestParam String code) {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

    OAuth2TokenRequest oAuth2TokenRequest = OAuth2TokenRequest.builder()
        .code(code)
        .redirectUri(UriConstants.Full.CALLBACK_URL_PREFIX + registrationId)
        .build();

    return webClientWrappable.getOAuth2TokenResponse(clientRegistration, oAuth2TokenRequest);
  }
}
