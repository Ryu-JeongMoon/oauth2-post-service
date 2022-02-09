package com.support.oauth2postservice.service.member.dto.request;

import com.support.oauth2postservice.util.constant.RegexpConstants;
import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.member.entity.Member;
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

    private LoginType loginType;

    @Builder
    public MemberSignupRequest(String nickname, String email, String password, LoginType loginType) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .loginType(this.loginType)
                .build();
    }
}
