package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequest {

  @Size(max = ColumnConstants.Length.ID)
  @Schema(description = "고유 아이디", example = "d763785c-f33f-4bba-b50e-fed6a9cc41ff", minLength = 36, maxLength = 36, required = true, title = "UUID")
  private String id;

  @Size(max = ColumnConstants.Length.NICKNAME_MAX)
  @Schema(description = "닉네임", example = "panda", minLength = 1, maxLength = 20)
  private String nickname;

  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  @Schema(description = "비밀번호", example = "1234", minLength = 4, maxLength = 255)
  private String password;

  @Schema(description = "권한", example = "USER", allowableValues = {"USER", "MANAGER", "ADMIN"})
  private Role role;

  @Schema(description = "활성 상태", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
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
