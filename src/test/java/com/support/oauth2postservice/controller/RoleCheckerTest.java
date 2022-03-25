package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.config.AbstractDataJpaTest;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RoleCheckerTest extends AbstractDataJpaTest {

  Member member;
  RoleChecker roleChecker;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    memberRepository.save(member);

    Authentication authentication = UserPrincipal.from(member).toAuthentication();
    SecurityUtils.setAuthentication(authentication);

    roleChecker = new RoleChecker(memberRepository);
  }

  @Nested
  @DisplayName("본인 확인")
  class IsOwnerTest {

    @Test
    @DisplayName("성공")
    void isOwner() {
      boolean isOwner = roleChecker.isOwner(member.getId());

      assertTrue(isOwner);
    }

    @Test
    @DisplayName("실패 - 회원 아이디 불일치")
    void isOwner_failByWrongId() {
      boolean isOwner = roleChecker.isOwner("YAHOO");

      assertFalse(isOwner);
    }
  }

  @Nested
  @DisplayName("권한 확인")
  class IsAuthorizedTest {

    @Test
    @DisplayName("성공 - 본인")
    void isAuthorized() {
      boolean isAuthorized = roleChecker.isAuthorized(member.getId());

      assertTrue(isAuthorized);
    }

    @Test
    @DisplayName("성공 - 관리자")
    void isAuthorized_successByAdmin() {
      // given
      Member admin = MemberTestHelper.createAdmin();
      memberRepository.save(admin);

      Authentication authentication = UserPrincipal.from(admin).toAuthentication();
      SecurityUtils.setAuthentication(authentication);

      // when
      boolean isAuthorized = roleChecker.isAuthorized(member.getId());

      // then
      assertTrue(isAuthorized);
    }

    @Test
    @DisplayName("실패 - 본인이 아닌 회원")
    void isAuthorized_failBy() {
      // given
      Member customMember = MemberTestHelper.createCustomUser("panda@nate.com");
      memberRepository.save(customMember);

      Authentication authentication = UserPrincipal.from(customMember).toAuthentication();
      SecurityUtils.setAuthentication(authentication);

      // when
      boolean isAuthorized = roleChecker.isAuthorized(member.getId());

      // then
      assertFalse(isAuthorized);
    }
  }
}