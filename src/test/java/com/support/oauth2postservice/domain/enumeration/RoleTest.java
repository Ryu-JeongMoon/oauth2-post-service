package com.support.oauth2postservice.domain.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

  @Test
  @DisplayName("key 출력 확인")
  void getKey() {
    assertThat(Role.USER.getKey()).isEqualTo("ROLE_USER");
    assertThat(Role.ADMIN.getKey()).isEqualTo("ROLE_ADMIN");
    assertThat(Role.MANAGER.getKey()).isEqualTo("ROLE_MANAGER");
  }
}