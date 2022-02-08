package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.service.member.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;

public class MemberTestHelper {

    public static final String PASSWORD = "password";
    public static final String USER_NAME = "user";
    public static final String USER_EMAIL = "user@gmail.com";
    public static final String MANAGER_NAME = "manager";
    public static final String MANGER_EMAIL = "manager@gmail.com";
    public static final String ADMIN_NAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";

    public static Member createUser() {
        return Member.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(PASSWORD)
                .build();
    }

    public static Member createManger() {
        return Member.builder()
                .name(MANAGER_NAME)
                .email(MANGER_EMAIL)
                .password(PASSWORD)
                .role(Role.MANAGER)
                .build();
    }

    public static Member createAdmin() {
        return Member.builder()
                .name(ADMIN_NAME)
                .email(ADMIN_EMAIL)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .build();
    }

    public static MemberSignupRequest createUserRequest() {
        return MemberSignupRequest.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(PASSWORD)
                .build();
    }

    public static MemberSignupRequest createManagerRequest() {
        return MemberSignupRequest.builder()
                .name(MANAGER_NAME)
                .email(MANGER_EMAIL)
                .password(PASSWORD)
                .build();
    }

    public static MemberEditRequest createEditRequest() {
        return MemberEditRequest.builder()
                .name(ADMIN_NAME)
                .password(PASSWORD + PASSWORD)
                .build();
    }
}
