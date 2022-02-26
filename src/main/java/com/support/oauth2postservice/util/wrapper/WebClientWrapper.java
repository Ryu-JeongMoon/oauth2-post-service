package com.support.oauth2postservice.util.wrapper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Primary
@Component
@RequiredArgsConstructor
public class WebClientWrapper implements WebClientWrappable {

  private static final String VERIFICATION_URI = "https://www.googleapis.com/oauth2/v3/tokeninfo";
  private static final String ERROR = "error";

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
            this::convertToAuthenticationException
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

  private Mono<OAuth2AuthenticationException> convertToAuthenticationException(ClientResponse clientResponse) {
    return clientResponse
        .bodyToMono(String.class)
        .map(OAuth2AuthenticationException::new);
  }

  @Override
  public boolean validateByOAuth2(String token) {
    return webClient.mutate().baseUrl(VERIFICATION_URI).build()
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParam(TokenConstants.ACCESS_TOKEN, token)
            .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            this::convertToAuthenticationException
        )
        .bodyToMono(String.class)
        .map(response -> !response.contains(ERROR))
        .flux()
        .toStream()
        .findAny()
        .orElseGet(() -> false);
  }
}
