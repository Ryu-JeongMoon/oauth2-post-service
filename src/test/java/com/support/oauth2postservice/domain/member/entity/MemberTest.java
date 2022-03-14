package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

  Member user;
  Member manager;
  Member admin;

  @BeforeEach
  void setUp() {
    user = MemberTestHelper.createUser();
    manager = MemberTestHelper.createManger();
    admin = MemberTestHelper.createAdmin();
  }

  @Test
  @DisplayName("LoginType 기본 값 확인")
  void defaultLoginType() {
    assertThat(user.getInitialAuthProvider()).isEqualTo(AuthProvider.LOCAL);
  }

  @Test
  @DisplayName("Role & Status 기본 값 확인")
  void roleAndStatus() {
    assertThat(user.getRole()).isEqualTo(Role.USER);
    assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
  }

  @Test
  @DisplayName("비밀번호 암호화 성공")
  void encodePassword() {
    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    String rawPassword = "1234";
    String encodedPassword = passwordEncoder.encode(rawPassword);
    user.changeToEncodedPassword(encodedPassword);

    boolean isMatched = passwordEncoder.matches("1234", user.getPassword());
    assertThat(isMatched).isEqualTo(true);
  }

  @Test
  @DisplayName("탈퇴 성공")
  void leave() {
    user.leave();

    assertThat(user.getLeftAt()).isNotNull();
  }

  @Test
  @DisplayName("탈퇴 실패 - 중복 탈퇴 불가")
  void leaveFailByDuplication() {
    user.leave();

    assertThrows(IllegalStateException.class, () -> user.leave());
  }

  @Nested
  @DisplayName("개인정보 변경")
  class editMemberInfoTest {

    @Test
    @DisplayName("닉네임 변경 성공")
    void editInfo() {
      user.changeBy("AAAA", null, null);

      assertThat(user.getNickname()).isEqualTo("AAAA");
    }

    @Test
    @DisplayName("권한 변경 성공")
    void changeRole() {
      user.changeBy(null, Role.MANAGER, null);

      assertThat(user.getRole()).isEqualTo(Role.MANAGER);
    }

    @Test
    @DisplayName("활성 상태 변경 성공")
    void changeStatus() {
      user.changeBy(null, null, Status.INACTIVE);

      assertThat(user.getStatus()).isEqualTo(Status.INACTIVE);
    }
  }
}