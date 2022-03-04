package com.support.oauth2postservice.util.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimesTest {

  @Test
  @DisplayName("Times 최댓값 604_800_000 - int 표현 가능")
  void time() {
    int value = Times.REFRESH_TOKEN_EXPIRATION_MILLIS.getValue();

    Assertions.assertThat(value).isLessThan(Integer.MAX_VALUE);
  }
}