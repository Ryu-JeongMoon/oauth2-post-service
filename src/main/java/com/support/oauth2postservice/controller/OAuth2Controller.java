package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private final WebClient webClient;
  private final ClientRegistrationRepository clientRegistrationRepository;

  @ResponseBody
  @GetMapping(UriConstants.Mapping.OAUTH2_CALLBACK)
  public String oauth2Callback(@PathVariable String registrationId, @RequestParam String code) {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

    OAuth2TokenRequest oAuth2TokenRequest = OAuth2TokenRequest.builder()
        .code(code)
        .redirectUri(UriConstants.Full.CALLBACK_URL_PREFIX + registrationId)
        .build();

    OAuth2TokenResponse oAuth2TokenResponse = webClient.post()
        .uri(clientRegistration.getProviderDetails().getTokenUri())
        .headers(httpHeader -> configureDefaultHeaders(httpHeader, clientRegistration))
        .bodyValue(oAuth2TokenRequest.toFormData())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> clientResponse
                .bodyToMono(String.class)
                .map(OAuth2AuthenticationException::new)
        )
        .bodyToMono(OAuth2TokenResponse.class)
        .flux()
        .toStream()
        .findFirst()
        .orElseGet(() -> null);

    return Objects.requireNonNull(oAuth2TokenResponse).toString();
  }

  private void configureDefaultHeaders(HttpHeaders httpHeaders, ClientRegistration clientRegistration) {
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
    httpHeaders.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
  }
}
