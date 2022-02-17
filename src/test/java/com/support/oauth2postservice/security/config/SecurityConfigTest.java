package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.util.constant.ColumnConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

  @Test
  @DisplayName("기본 Argon2id Encoding 시 입력값과 상관 없이 결과 값 96 byte")
  void passwordEncoding() {
    Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();
    String p = "panda";

    String encode1 = argon2PasswordEncoder.encode(p);
    String encode2 = argon2PasswordEncoder.encode(p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p);
    String encode3 = argon2PasswordEncoder.encode(p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p);
    String encode4 = argon2PasswordEncoder.encode(p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p
        + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p + p);

    assertThat(encode1.length()).isEqualTo(ColumnConstants.Length.ENCODED_PASSWORD);
    assertThat(encode2.length()).isEqualTo(ColumnConstants.Length.ENCODED_PASSWORD);
    assertThat(encode3.length()).isEqualTo(ColumnConstants.Length.ENCODED_PASSWORD);
    assertThat(encode4.length()).isEqualTo(ColumnConstants.Length.ENCODED_PASSWORD);
  }

  @Test
  @DisplayName("커스텀 Argon2 Encoding 시 결과 값 달라질 수 있다")
  void customArgonPasswordEncoder() {
    Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(32, 32, 2, 1 << 13, 3);

    String b = "bear";

    String encode1 = argon2PasswordEncoder.encode(b + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b);
    String encode2 = argon2PasswordEncoder.encode(b + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b
        + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b
        + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b
        + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b + b);

    assertThat(encode1.length()).isEqualTo(117);
    assertThat(encode2.length()).isEqualTo(117);
  }
}