package com.support.oauth2postservice.learning;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumUtilsTest {

  @Test
  @DisplayName("Enum - 소문자로 생성")
  void fromIgnoreCaseByLowerCase() {
    String lowerCase = "local";

    AuthProvider authProvider = AuthProvider.toEnum(lowerCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("Enum - 대문자로 생성")
  void fromIgnoreCaseByUpperCase() {
    String upperCase = "LOCAL";

    AuthProvider authProvider = AuthProvider.toEnum(upperCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("Enum - 혼합 문자로 생성")
  void fromIgnoreCaseByMixedCase() {
    String mixedCase = "LoCaL";

    AuthProvider authProvider = AuthProvider.toEnum(mixedCase);

    assertThat(authProvider).isNotNull();
  }

  @Test
  @DisplayName("존재하지 않는 인자 입력 시 IllegalArgumentException 발생")
  void fromIgnoreCaseByNonExistsString() {
    String nonExistsString = "LoCaLLL";

    assertThrows(
        IllegalArgumentException.class,
        () -> AuthProvider.toEnum(nonExistsString)
    );
  }
}