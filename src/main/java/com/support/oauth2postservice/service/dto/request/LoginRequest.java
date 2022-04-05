package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.RegexpConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

  @Email
  @NotBlank
  @Size(max = ColumnConstants.Length.EMAIL_MAX)
  @Schema(description = "이메일", example = "panda@gmail.com", pattern = RegexpConstants.EMAIL, required = true)
  private String email;

  @Size(min = ColumnConstants.Length.PASSWORD_MIN, max = ColumnConstants.Length.DEFAULT_MAX)
  @Schema(description = "비밀번호", example = "1234", minLength = 4, maxLength = 255, required = true)
  private String password;

  @Builder
  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
