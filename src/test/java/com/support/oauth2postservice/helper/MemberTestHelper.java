package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;

import java.util.UUID;

public class MemberTestHelper {

  public static final String PASSWORD = "password";
  public static final String USER_NICKNAME = "user";
  public static final String USER_EMAIL = "user@gmail.com";
  public static final String USER_NICKNAME_AFTER_EDIT = "newUser";
  public static final String MANAGER_NICKNAME = "manager";
  public static final String MANGER_EMAIL = "manager@gmail.com";
  public static final String ADMIN_NICKNAME = "admin";
  public static final String ADMIN_EMAIL = "admin@gmail.com";

  public static Member createCustomUser(String email) {
    return Member.builder()
        .nickname(USER_NICKNAME)
        .email(email)
        .password(PASSWORD)
        .build();
  }

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

  public static MemberSignupRequest createUserSignupRequest() {
    return MemberSignupRequest.builder()
        .nickname(USER_NICKNAME)
        .email(USER_EMAIL)
        .password(PASSWORD)
        .build();
  }

  public static MemberSignupRequest createManagerSignupRequest() {
    return MemberSignupRequest.builder()
        .nickname(MANAGER_NICKNAME)
        .email(MANGER_EMAIL)
        .password(PASSWORD)
        .build();
  }

  public static MemberEditRequest createDefaultEditRequest() {
    return MemberEditRequest.builder()
        .id(UUID.randomUUID().toString())
        .nickname(USER_NICKNAME_AFTER_EDIT)
        .password(PASSWORD + PASSWORD)
        .build();
  }

  public static MemberEditRequest createSpecificEditRequest(String memberId) {
    return MemberEditRequest.builder()
        .id(memberId)
        .nickname(USER_NICKNAME_AFTER_EDIT)
        .password(PASSWORD + PASSWORD)
        .build();
  }

  public static MemberDeleteRequest createDeleteRequest(String memberId) {
    return MemberDeleteRequest.builder()
        .id(memberId)
        .password(PASSWORD)
        .build();
  }

  public static MemberReadResponse createReadResponse(Member member) {
    return MemberReadResponse.builder()
        .id(member.getId())
        .role(member.getRole())
        .email(member.getEmail())
        .status(member.getStatus())
        .nickname(member.getNickname())
        .latestAuthProvider(member.getLatestAuthProvider())
        .build();
  }

  public static MemberReadResponse createDefaultResponse() {
    return MemberReadResponse.builder()
        .id(UUID.randomUUID().toString())
        .role(Role.USER)
        .email(USER_EMAIL)
        .status(Status.ACTIVE)
        .nickname(USER_NICKNAME)
        .latestAuthProvider(AuthProvider.GOOGLE)
        .build();
  }
}
