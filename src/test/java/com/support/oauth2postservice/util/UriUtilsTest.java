package com.support.oauth2postservice.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UriUtilsTest {

  @Test
  @DisplayName("uri 변환 성공")
  void toGoogle() {
    String targetUri = UriUtils.toGoogle("www.{registrationId}.com");

    Assertions.assertThat(targetUri).isEqualTo("www.google.com");
  }

  @Test
  @DisplayName("uri 변환 실패 - 중괄호로 감싼 형태로 정의되어 있어야 한다")
  void toGoogle_failByWrongUri() {
    String targetUri = UriUtils.toGoogle("www.registrationId.com");

    Assertions.assertThat(targetUri).isNotEqualTo("www.google.com");
  }
}