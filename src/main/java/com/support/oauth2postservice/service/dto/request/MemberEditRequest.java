package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequest {

  private String id;

  @Size(max = ColumnConstants.Length.NICKNAME)
  private String nickname;

  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
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
