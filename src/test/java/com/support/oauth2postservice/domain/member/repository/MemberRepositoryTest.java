package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends JpaTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = MemberTestHelper.createUser();
        USER_ID = memberRepository.save(member).getId();
    }

    @Test
    @DisplayName("EntityListener 작동")
    void entityListener() {
        Member member = Member.builder()
                .name("panda")
                .email("bear")
                .password("1234")
                .build();
        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getCreatedAt()).isNotNull();
        assertThat(savedMember.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("활성 상태 회원 조회")
    void findActiveMember() {
        Member member = memberRepository.findActiveMember(USER_ID)
                .orElseGet(() -> null);

        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("비활성 상태 회원 조회")
    void findInactiveMember() {
        Member member = memberRepository.findActiveMember(USER_ID).get();
        member.leave();

        Member findResult = memberRepository.findActiveMember(member.getId())
                .orElseGet(() -> null);

        assertThat(findResult).isNull();
    }
}