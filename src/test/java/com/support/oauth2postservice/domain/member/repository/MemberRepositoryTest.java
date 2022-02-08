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
    @DisplayName("EntityListener 작동")
    void entityListener() {
        Member member = memberRepository.findActive(USER_ID)
                .orElseGet(() -> null);

        assertThat(member.getCreatedAt()).isNotNull();
        assertThat(member.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("활성 상태 회원 조회")
    void findActiveMember() {
        Member member = memberRepository.findActive(USER_ID)
                .orElseGet(() -> null);

        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("비활성 상태 회원 조회")
    void findInactiveMember() {
        Member member = memberRepository.findActive(USER_ID).get();
        member.leave();

        Member findResult = memberRepository.findActive(member.getId())
                .orElseGet(() -> null);

        assertThat(findResult).isNull();
    }

    @Test
    @DisplayName("")
    void findActiveByNickname() {
        memberRepository.findActiveByNickname(MemberTestHelper.USER_NAME)
                .orElseThrow(RuntimeException::new);
    }
}