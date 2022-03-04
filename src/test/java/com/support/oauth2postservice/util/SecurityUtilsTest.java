package com.support.oauth2postservice.util;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUtilsTest {

  @BeforeEach
  void setUp() {
    Member member = MemberTestHelper.createUser();
    UserPrincipal userPrincipal = UserPrincipal.from(member);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, "");
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  /**
   * Id 는 DB 에 저장된 후 확인 가능하기에 <b>MemberRepositoryTest</b> 에서 추가 확인
   */
  @Test
  @DisplayName("Id 반환 실패 - DB 저장 되기 전 ID 값은 Null")
  void getIdFromCurrentUser_failByEmptyId() {
    String id = SecurityUtils.getIdFromCurrentUser();

    assertThat(id).isBlank();
  }

  @Test
  @DisplayName("Email 반환 - ContextHolder 에 저장되어있는 값 반환")
  void getEmailFromCurrentUser() {
    String email = SecurityUtils.getEmailFromCurrentUser();

    assertThat(email).isNotBlank();
  }
}