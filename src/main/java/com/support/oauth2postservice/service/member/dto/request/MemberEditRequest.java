package com.support.oauth2postservice.service.member.dto.request;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequest {

  private String id;

  private String nickname;

  private String password;

  private Role role;

  private Status status;

  @Builder
  public MemberEditRequest(String id, String nickname, String password, Role role, Status status) {
    this.id = id;
    this.nickname = nickname;
    this.password = password;
    this.role = role;
    this.status = status;
  }
}
