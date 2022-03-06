package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;

public class MemberTestHelper {

  public static final String PASSWORD = "password";
  public static final String USER_NICKNAME = "user";
  public static final String USER_EMAIL = "user@gmail.com";
  public static final String USER_NICKNAME_AFTER_EDIT = "newUser";
  public static final String MANAGER_NICKNAME = "manager";
  public static final String MANGER_EMAIL = "manager@gmail.com";
  public static final String ADMIN_NICKNAME = "admin";
  public static final String ADMIN_EMAIL = "admin@gmail.com";

  public static Member createUser() {
    return Member.builder()
        .nickname(USER_NICKNAME)
        .email(USER_EMAIL)
        .password(PASSWORD)
        .build();
  }

  public static Member createManger() {
    return Member.builder()
        .nickname(MANAGER_NICKNAME)
        .email(MANGER_EMAIL)
        .password(PASSWORD)
        .role(Role.MANAGER)
        .build();
  }

  public static Member createAdmin() {
    return Member.builder()
        .nickname(ADMIN_NICKNAME)
        .email(ADMIN_EMAIL)
        .password(PASSWORD)
        .role(Role.ADMIN)
        .build();
  }

  public static MemberSignupRequest createUserRequest() {
    return MemberSignupRequest.builder()
        .nickname(USER_NICKNAME)
        .email(USER_EMAIL)
        .password(PASSWORD)
        .build();
  }

  public static MemberSignupRequest createManagerRequest() {
    return MemberSignupRequest.builder()
        .nickname(MANAGER_NICKNAME)
        .email(MANGER_EMAIL)
        .password(PASSWORD)
        .build();
  }

  public static MemberEditRequest createEditRequest() {
    return MemberEditRequest.builder()
        .nickname(USER_NICKNAME_AFTER_EDIT)
        .password(PASSWORD + PASSWORD)
        .build();
  }
}
