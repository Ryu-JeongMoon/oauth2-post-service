package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends JpaTest {

    @Autowired
    MemberRepository memberRepository;

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
}