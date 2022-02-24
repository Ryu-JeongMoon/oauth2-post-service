package com.support.oauth2postservice.learning;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnumUtilsTest {

  @Test
  @DisplayName("Enum - 소문자로 생성")
  void fromIgnoreCaseByLowerCase() {
    String lowerCase = "local";

    AuthProvider authProvider = EnumUtils.getEnumIgnoreCase(AuthProvider.class, lowerCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("Enum - 대문자로 생성")
  void fromIgnoreCaseByUpperCase() {
    String upperCase = "LOCAL";

    AuthProvider authProvider = EnumUtils.getEnumIgnoreCase(AuthProvider.class, upperCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("Enum - 혼합 문자로 생성")
  void fromIgnoreCaseByMixedCase() {
    String mixedCase = "LoCaL";

    AuthProvider authProvider = EnumUtils.getEnumIgnoreCase(AuthProvider.class, mixedCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("존재하지 않는 인자 입력 시 null 반환")
  void fromIgnoreCaseByNonExistsString() {
    String nonExistsString = "LoCaLLL";

    AuthProvider authProvider = EnumUtils.getEnumIgnoreCase(AuthProvider.class, nonExistsString);

    assertThat(authProvider).isNull();
  }
}