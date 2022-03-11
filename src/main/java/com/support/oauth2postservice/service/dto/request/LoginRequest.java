package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.util.constant.ColumnConstants;
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
  @Size(max = ColumnConstants.Length.EMAIL)
  private String email;

  @Size(min = ColumnConstants.Length.PASSWORD_MIN, max = ColumnConstants.Length.DEFAULT_MAX)
  private String password;

  @Builder
  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
