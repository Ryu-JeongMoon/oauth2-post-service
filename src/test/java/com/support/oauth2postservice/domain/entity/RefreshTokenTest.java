package com.support.oauth2postservice.domain.entity;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.RefreshTokenTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

  @Test
  @DisplayName("토큰 값 변경")
  void changeTokenValue() {
    RefreshToken refreshToken = RefreshToken.builder()
        .member(MemberTestHelper.createUser())
        .expiredAt(LocalDateTime.MAX)
        .authProvider(AuthProvider.GOOGLE)
        .tokenValue("YAHOO")
        .build();

    refreshToken.changeTokenValue("GOOGLE");

    assertThat(refreshToken.getTokenValue()).isEqualTo("GOOGLE");
  }

  @Test
  @DisplayName("메서드 체이닝을 위한 토큰 변경 후 반환")
  void withTokenValue() {
    RefreshToken refreshToken = RefreshTokenTestHelper.create();
    RefreshToken newRefreshToken = refreshToken.withTokenValue("YAHOO");

    assertThat(refreshToken.getTokenValue()).isEqualTo(newRefreshToken.getTokenValue());
  }
}