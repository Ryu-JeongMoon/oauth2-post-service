package com.support.oauth2postservice.util.wrapper;

import com.support.oauth2postservice.security.oauth2.OAuth2TokenRequest;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

  private final WebClient webClient;

  @Override
  public OAuth2TokenResponse getOAuth2TokenResponse(OAuth2TokenRequest oAuth2TokenRequest) {
    return webClient.mutate().baseUrl(UriConstants.Full.TOKEN_REQUEST_URI).build()
        .post()
        .headers(this::configureDefaultHeaders)
        .bodyValue(oAuth2TokenRequest.toFormData())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            this::convertToOAuth2AuthenticationException
        )
        .bodyToMono(OAuth2TokenResponse.class)
        .onErrorReturn(OAuth2TokenResponse.empty())
        .flux()
        .toStream()
        .findAny()
        .orElseGet(OAuth2TokenResponse::empty);
  }

  private void configureDefaultHeaders(HttpHeaders httpHeaders) {
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
  }

  private Mono<OAuth2AuthenticationException> convertToOAuth2AuthenticationException(ClientResponse clientResponse) {
    return clientResponse
        .bodyToMono(String.class)
        .map(OAuth2AuthenticationException::new);
  }

  @Override
  public OAuth2TokenResponse renewAccessToken(OAuth2TokenRequest oAuth2TokenRequest) {
    return webClient.mutate().baseUrl(UriConstants.Full.TOKEN_REQUEST_URI).build()
        .post()
        .headers(this::configureDefaultHeaders)
        .bodyValue(oAuth2TokenRequest.toFormData())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            this::convertToOAuth2AuthenticationException
        )
        .bodyToMono(OAuth2TokenResponse.class)
        .onErrorReturn(OAuth2TokenResponse.empty())
        .flux()
        .toStream()
        .findAny()
        .orElseGet(OAuth2TokenResponse::empty);
  }

  @Override
  public boolean validateByOidc(String idToken) {
    return webClient.mutate().baseUrl(UriConstants.Full.VERIFICATION_URI).build()
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParam(TokenConstants.ID_TOKEN, idToken)
            .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            this::convertToOAuth2AuthenticationException
        )
        .bodyToMono(String.class)
        .map(response -> !response.contains(TokenConstants.ERROR))
        .onErrorReturn(OAuth2AuthenticationException.class, false)
        .flux()
        .toStream()
        .findAny()
        .orElseGet(() -> false);
  }
}
