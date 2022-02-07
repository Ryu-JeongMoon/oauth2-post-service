package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("LoginType 기본 값 확인")
    void defaultLoginType() {
        Member member = Member.builder().build();

        assertThat(member.getLoginType()).isEqualTo(LoginType.LOCAL);
    }

    @Test
    @DisplayName("Role & Status 기본 값 확인")
    void roleAndStatus() {
        Member member = Member.builder().build();

        assertThat(member.getRole()).isEqualTo(Role.USER);
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }
}