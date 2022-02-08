package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        assertThat(user.getLoginType()).isEqualTo(LoginType.LOCAL);
    }

    @Test
    @DisplayName("Role & Status 기본 값 확인")
    void roleAndStatus() {
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("권한 변경 시 일반 회원으로 시도 - 권한 부족")
    void failChangeRoleByUser() {
        assertThrows(AccessDeniedException.class, () -> user.changeRole(Role.ADMIN));
    }

    @Test
    @DisplayName("권한 변경 시 매니저로 관리자 권한 변경 시도 - 권한 부족")
    void failChangeRoleByManager() {
        assertThrows(AccessDeniedException.class, () -> manager.changeRole(Role.ADMIN));
    }

    @Test
    @DisplayName("비밀번호 암호화 성공")
    void encodePassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.encodePassword(passwordEncoder, "1234");

        boolean isMatched = passwordEncoder.matches("1234", user.getPassword());
        assertThat(isMatched).isEqualTo(true);
    }

    @Test
    @DisplayName("개인정보 수정 성공")
    void editInfo() {
        user.editInfo("AAAA", null, null);

        assertThat(user.getName()).isEqualTo("AAAA");
    }

    @Test
    @DisplayName("개인정보 수정 실패 - 권한 부족")
    void failEditInfoByRole() {
        assertThrows(AccessDeniedException.class,
                () -> user.editInfo("AAAA", Role.MANAGER, null));
    }


    @Test
    @DisplayName("탈퇴 성공")
    void leave() {
        user.leave();

        assertThat(user.getLeftAt()).isNotNull();
    }

    @Test
    @DisplayName("탈퇴 실패 - 중복 탈퇴 불가")
    void failLeave() {
        user.leave();

        assertThrows(IllegalStateException.class, () -> user.leave());
    }

    @Test
    @DisplayName("권한 변경 성공")
    void changeRole() {
        manager.changeRole(Role.USER);

        assertThat(manager.getRole()).isEqualTo(Role.USER);
    }
}