package com.support.oauth2postservice.security.oauth2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomOAuth2ProviderTest {

  @Test
  @DisplayName("valueOf 사용 시 대문자 생성 허용")
  void enumValueOf() {
    CustomOAuth2Provider google = CustomOAuth2Provider.valueOf("google".toUpperCase());
    Assertions.assertThat(google).isNotNull();
  }

  @Test
  @DisplayName("valueOf 사용 시 소문자 생성 불가")
  void enumValueOfFailByLowerCase() {
    assertThrows(IllegalArgumentException.class,
        () -> CustomOAuth2Provider.valueOf("google"));
  }
}