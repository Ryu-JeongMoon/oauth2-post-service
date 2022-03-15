package com.support.oauth2postservice.domain.entity;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
}