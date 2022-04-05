package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.util.constant.ColumnConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDeleteRequest {

  @NotBlank
  @Size(max = ColumnConstants.Length.ID)
  @Schema(description = "고유 번호", example = "d763785c-f33f-4bba-b50e-fed6a9cc41ff", minLength = 36, maxLength = 36, required = true, title = "UUID")
  private String id;

  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  @Schema(description = "비밀번호", example = "1234", minLength = 4, maxLength = 255, required = true)
  private String password;

  @Builder
  public MemberDeleteRequest(String id, String password) {
    this.id = id;
    this.password = password;
  }
}
