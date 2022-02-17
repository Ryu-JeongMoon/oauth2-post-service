package com.support.oauth2postservice.service.member.dto.request;

import com.support.oauth2postservice.domain.member.entity.Member;
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

  @Size(min = 1, max = 20)
  private String nickname;

  @Size(min = 7, max = 320)
  @Email(regexp = RegexpConstants.EMAIL)
  private String email;

  @Size(min = 4, max = 255)
  private String password;

  @Builder
  public MemberSignupRequest(String nickname, String email, String password) {
    this.nickname = nickname;
    this.email = email;
    this.password = password;
  }

  public Member toEntity() {
    return Member.builder()
        .nickname(this.nickname)
        .email(this.email)
        .password(this.password)
        .build();
  }
}
