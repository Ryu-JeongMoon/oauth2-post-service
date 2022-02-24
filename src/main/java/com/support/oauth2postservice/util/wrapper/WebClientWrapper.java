package com.support.oauth2postservice.util.wrapper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Primary
@Component
@RequiredArgsConstructor
public class WebClientWrapper implements WebClientWrappable {

  private final WebClient webClient;

  @Override
  public OAuth2TokenResponse getOAuth2TokenResponse(ClientRegistration clientRegistration, OAuth2TokenRequest oAuth2TokenRequest) {
    return webClient.post()
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
        .findAny()
        .orElseThrow(() -> new OAuth2AuthenticationException(ExceptionMessages.TOKEN_REQUEST_REJECTED));
  }

  private void configureDefaultHeaders(HttpHeaders httpHeaders, ClientRegistration clientRegistration) {
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
    httpHeaders.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
  }
}
