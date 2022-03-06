package com.support.oauth2postservice.service.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReadResponse {

  private String id;

  private String email;

  private String nickname;

  private Role role;

  private Status status;

  private AuthProvider latestAuthProvider;

  @Builder
  @QueryProjection
  public MemberReadResponse(String id, String email, String nickname, Role role, Status status, AuthProvider latestAuthProvider) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.role = role;
    this.status = status;
    this.latestAuthProvider = latestAuthProvider;
  }

  public static MemberReadResponse from(Member member) {
    return MemberReadResponse.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .role(member.getRole())
        .status(member.getStatus())
        .latestAuthProvider(member.getLatestAuthProvider())
        .build();
  }
}
