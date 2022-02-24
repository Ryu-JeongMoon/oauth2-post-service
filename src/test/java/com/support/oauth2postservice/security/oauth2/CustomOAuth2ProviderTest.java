package com.support.oauth2postservice.security.oauth2;

import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomOAuth2ProviderTest {

  @Test
  @DisplayName("valueOf 사용 시 대문자 생성 허용")
  void enumValueOf() {
    CustomOAuth2Provider google = CustomOAuth2Provider.valueOf("google".toUpperCase());
    assertThat(google).isNotNull();
  }

  @Test
  @DisplayName("valueOf 사용 시 소문자 생성 불가")
  void enumValueOfFailByLowerCase() {
    assertThrows(IllegalArgumentException.class,
        () -> CustomOAuth2Provider.valueOf("google"));
  }

  @Test
  @DisplayName("EnumUtils getEnumIgnoreCase 사용")
  void enumUtils() {
    CustomOAuth2Provider oAuth2Provider = EnumUtils.getEnumIgnoreCase(CustomOAuth2Provider.class, "google");

    assertThat(oAuth2Provider).isNotNull();
  }
}