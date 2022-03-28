package com.support.oauth2postservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.util.constant.RegexpConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignupRequest {

  @Size(min = 7, max = 320)
  @Email(regexp = RegexpConstants.EMAIL)
  @Schema(description = "이메일", example = "panda@gmail.com", pattern = RegexpConstants.EMAIL, required = true)
  private String email;

  @Size(min = 1, max = 20)
  @Schema(description = "닉네임", example = "panda", minLength = 1, maxLength = 20, required = true)
  private String nickname;

  @Size(min = 4, max = 255)
  @Schema(description = "비밀번호", example = "1234", minLength = 4, maxLength = 255, required = true)
  private String password;

  @Builder
  @JsonCreator
  public MemberSignupRequest(String email, String nickname, String password) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
  }

  public Member toEntity() {
    return Member.builder()
        .email(this.email)
        .nickname(this.nickname)
        .password(this.password)
        .initialAuthProvider(AuthProvider.LOCAL)
        .build();
  }
}
