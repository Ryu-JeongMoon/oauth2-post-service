package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private final WebClientWrappable webClientWrappable;
  private final ClientRegistrationRepository clientRegistrationRepository;

  @GetMapping(UriConstants.Mapping.REISSUE_TOKEN)
  public OAuth2TokenResponse getOAuth2Token(@PathVariable String registrationId, @RequestParam String code) {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

    OAuth2TokenRequest oAuth2TokenRequest = OAuth2TokenRequest.builder()
        .code(code)
        .clientId(clientRegistration.getClientId())
        .clientSecret(clientRegistration.getClientSecret())
        .redirectUri(UriConstants.Full.TOKEN_CALLBACK_URI)
        .build();

    return webClientWrappable.getOAuth2TokenResponse(oAuth2TokenRequest);
  }

  @GetMapping(UriConstants.Mapping.VALIDATE_TOKEN)
  @ResponseBody
  public boolean validate(@RequestParam(TokenConstants.ID_TOKEN) String idToken) {
    return webClientWrappable.validateByOidc(idToken);
  }
}