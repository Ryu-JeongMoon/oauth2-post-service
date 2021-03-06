package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.config.AbstractDataJpaTest;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends AbstractDataJpaTest {

  private Member member;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    USER_ID = memberRepository.save(member).getId();
  }

  @Test
  @DisplayName("UUID2 36 Byte 문자열 생성 확인")
  void validateGeneratedValueLength() {
    assertThat(USER_ID.length()).isEqualTo(36);
  }

  @Test
  @DisplayName("자동 생성 UUID2 기반 아이디 확인")
  void validateGeneratedValue() {
    Member member = MemberTestHelper.createUser();
    String USER_ID_2 = memberRepository.save(member).getId();

    assertThat(USER_ID).isNotEqualTo(USER_ID_2);
  }

  @Test
  @DisplayName("EntityListener 작동 확인")
  void entityListener() {
    Member member = memberRepository.findActiveById(USER_ID)
        .orElseGet(() -> Member.builder().build());

    assertThat(member.getCreatedAt()).isNotNull();
    assertThat(member.getModifiedAt()).isNotNull();
  }

  @Test
  @DisplayName("활성 상태 회원 조회 - PK")
  void findActiveMember() {
    assertThat(memberRepository.findActiveById(USER_ID).isPresent()).isTrue();
  }

  @Test
  @DisplayName("비활성 상태 회원 조회")
  void findInactiveMember() {
    Member member = memberRepository.findActiveById(USER_ID)
        .orElseGet(() -> Member.builder().build());
    member.leave();

    boolean isMemberPresent = memberRepository.findActiveById(member.getId()).isPresent();
    assertThat(isMemberPresent).isFalse();
  }

  @Test
  @DisplayName("활성 상태 회원 조회 - 닉네임")
  void findActiveByNickname() {
    boolean isMemberPresent = memberRepository.findActiveByNickname(MemberTestHelper.USER_NICKNAME).isPresent();

    assertThat(isMemberPresent).isTrue();
  }

  @Test
  @DisplayName("활성 상태 회원 조회 실패 - 닉네임")
  void failFindActiveByNickname() {
    boolean isMemberPresent = memberRepository.findActiveByNickname(MemberTestHelper.ADMIN_NICKNAME).isPresent();

    assertThat(isMemberPresent).isFalse();
  }

  @Test
  @DisplayName("equals proxy 비교 성공")
  void equals() {
    Member memberProxy = entityManager.getReference(Member.class, USER_ID);

    assertThat(member.equals(memberProxy)).isTrue();
  }

  @Test
  @DisplayName("equals 비교 - 다른 아이디 비교 시 False")
  void equalsFailByAnotherId() {
    Member admin = MemberTestHelper.createAdmin();
    memberRepository.save(admin);

    assertThat(member.equals(admin)).isFalse();
  }
}