package com.support.oauth2postservice.service;

import com.support.oauth2postservice.security.config.OAuth2Config;
import com.support.oauth2postservice.security.oauth2.OAuth2TokenResponse;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import(OAuth2Config.class)
class OAuth2TokenServiceTest extends ServiceTest {

  private final ClientRegistration testClientRegistration = ClientRegistration.withRegistrationId("panda")
      .tokenUri("panda")
      .clientId("panda")
      .clientSecret("panda")
      .clientName("panda")
      .redirectUri("panda")
      .authorizationUri("panda")
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      .build();

  @Mock
  WebClientWrappable webClientWrappable;
  @Mock
  ClientRegistrationRepository clientRegistrationRepository;
  @InjectMocks
  OAuth2TokenService oAuth2TokenService;

  @Nested
  @DisplayName("토큰 검증")
  class ValidateTest {
    @Test
    @DisplayName("토큰 검증 성공")
    void validate() {
      when(webClientWrappable.validateByOidc(any())).thenReturn(true);

      boolean validity = oAuth2TokenService.validate("yahoo");

      assertThat(validity).isTrue();
      verify(webClientWrappable, times(1)).validateByOidc(anyString());
    }

    @Test
    @DisplayName("토큰 검증 실패 - 빈 토큰 값으로 요청 시 false 반환")
    void validate_failByEmptyToken() {
      when(webClientWrappable.validateByOidc(eq(null))).thenReturn(false);

      boolean validity = oAuth2TokenService.validate(null);

      assertThat(validity).isFalse();
      verify(webClientWrappable, times(1)).validateByOidc(any());
    }
  }

  @Nested
  @DisplayName("토큰 반환")
  class GetOAuth2TokenTest {

    @Test
    @DisplayName("토큰 반환 성공")
    void getOAuth2Token() {
      when(clientRegistrationRepository.findByRegistrationId(any())).thenReturn(testClientRegistration);
      when(webClientWrappable.getToken(any())).thenReturn(OAuth2TokenResponse.empty());

      OAuth2TokenResponse oAuth2TokenResponse = oAuth2TokenService.getOAuth2Token("panda", "bear");

      assertThat(oAuth2TokenResponse).isNotNull();
      verify(webClientWrappable, times(1)).getToken(any());
    }

    @Test
    @DisplayName("토큰 반환 실패 - NPE, 존재하지 않는 registrationId 사용")
    void getOAuth2Token_failByWrongRegistrationId() {
      when(clientRegistrationRepository.findByRegistrationId(
          AdditionalMatchers.not((eq("google"))))).thenThrow(NullPointerException.class);

      assertThrows(NullPointerException.class,
          () -> oAuth2TokenService.getOAuth2Token("facebook", "yahoo"));

      verify(clientRegistrationRepository, times(1)).findByRegistrationId(any());
    }
  }

  @Nested
  @DisplayName("토큰 갱신")
  class RenewTest {

    @Test
    @DisplayName("토큰 갱신 성공")
    void renew() {
      when(clientRegistrationRepository.findByRegistrationId(any())).thenReturn(testClientRegistration);
      when(webClientWrappable.getRenewedToken(any())).thenReturn(OAuth2TokenResponse.empty());

      OAuth2TokenResponse oAuth2TokenResponse = oAuth2TokenService.renew("panda", "bear");

      assertThat(oAuth2TokenResponse).isNotNull();
      verify(clientRegistrationRepository, times(1)).findByRegistrationId(any());
      verify(webClientWrappable, times(1)).getRenewedToken(any());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - NPE, 존재하지 않는 registrationId 사용")
    void renew_failByWrongRegistrationId() {
      when(clientRegistrationRepository.findByRegistrationId(
          AdditionalMatchers.not((eq("google"))))).thenThrow(NullPointerException.class);

      assertThrows(NullPointerException.class,
          () -> oAuth2TokenService.renew("facebook", "yahoo"));
      verify(clientRegistrationRepository, times(1)).findByRegistrationId(any());
    }
  }
}