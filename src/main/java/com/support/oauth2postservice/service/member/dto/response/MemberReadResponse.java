package com.support.oauth2postservice.service.member.dto.response;

import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReadResponse {

    private String id;

    private String name;

    private String email;

    private Role role;

    private Status status;

    private LoginType loginType;

    @Builder
    public MemberReadResponse(String id, String name, String email, Role role, Status status, LoginType loginType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.loginType = loginType;
    }

    public static MemberReadResponse from(Member member) {
        return MemberReadResponse.builder()
                .id(member.getId())
                .name(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .status(member.getStatus())
                .loginType(member.getLoginType())
                .build();
    }
}
