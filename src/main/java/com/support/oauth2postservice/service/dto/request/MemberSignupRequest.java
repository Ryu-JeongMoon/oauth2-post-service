package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.util.constant.RegexpConstants;
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
  private String email;

  @Size(min = 1, max = 20)
  private String nickname;

  @Size(min = 4, max = 255)
  private String password;

  @Builder
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
