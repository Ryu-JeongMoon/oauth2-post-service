package com.support.oauth2postservice.util;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

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
    String id = SecurityUtils.getPrincipalFromCurrentUser().getId();

    assertThat(id).isBlank();
  }

  @Test
  @DisplayName("Email 반환 - ContextHolder 에 저장되어있는 값 반환")
  void getEmailFromCurrentUser() {
    String email = SecurityUtils.getPrincipalFromCurrentUser().getEmail();

    assertThat(email).isNotBlank();
  }

  @Test
  @DisplayName("Authentication 반환 성공")
  void getNullSafePrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    assertThat(authentication).isNotNull();
  }

  @Test
  @DisplayName("Authentication 세팅 성공")
  void setAuthentication() {
    // given
    Member admin = MemberTestHelper.createAdmin();
    UserPrincipal principal = UserPrincipal.from(admin);

    // when
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(principal, "", Collections.singletonList(Role.ADMIN));
    SecurityUtils.setAuthentication(authenticationToken);

    // then
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    assertThat(userPrincipal.getEmail()).isEqualTo(admin.getEmail());
  }
}