package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends JpaTest {

    @BeforeEach
    void setUp() {
        Member member = MemberTestHelper.createUser();
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
        final String USER_ID_2 = memberRepository.save(member).getId();

        assertThat(USER_ID).isNotEqualTo(USER_ID_2);
    }

    @Test
    @DisplayName("EntityListener 작동 확인")
    void entityListener() {
        Member member = memberRepository.findActive(USER_ID)
                .orElseGet(() -> Member.builder().build());

        assertThat(member.getCreatedAt()).isNotNull();
        assertThat(member.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("활성 상태 회원 조회 - PK")
    void findActiveMember() {
        assertThat(memberRepository.findActive(USER_ID).isPresent()).isTrue();
    }

    @Test
    @DisplayName("비활성 상태 회원 조회")
    void findInactiveMember() {
        Member member = memberRepository.findActive(USER_ID)
                .orElseGet(() -> Member.builder().build());
        member.leave();

        boolean isMemberPresent = memberRepository.findActive(member.getId()).isPresent();
        assertThat(isMemberPresent).isFalse();
    }

    @Test
    @DisplayName("활성 상태 회원 조회 - 닉네임")
    void findActiveByNickname() {
        boolean isMemberPresent = memberRepository.findActiveByNickname(MemberTestHelper.USER_NAME).isPresent();
        assertThat(isMemberPresent).isTrue();
    }

    @Test
    @DisplayName("활성 상태 회원 조회 실패 - 닉네임")
    void failFindActiveByNickname() {
        boolean isMemberPresent = memberRepository.findActiveByNickname(MemberTestHelper.ADMIN_NAME).isPresent();
        assertThat(isMemberPresent).isFalse();
    }
}