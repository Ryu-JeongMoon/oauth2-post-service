package com.support.oauth2postservice.service.member.dto.request;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequest {

  private String nickname;

  private String password;

  private Role role;

  private Status status;

  @Builder
  public MemberEditRequest(String nickname, String password, Role role, Status status) {
    this.nickname = nickname;
    this.password = password;
    this.role = role;
    this.status = status;
  }
}
