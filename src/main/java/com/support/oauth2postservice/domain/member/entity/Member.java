package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(length = 20)
    private String name;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(length = 10, name = "login_type")
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @Builder
    public Member(String name, String email, String password, LoginType loginType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.status = Status.ACTIVE;
        this.loginType = loginType != null ? loginType : LoginType.LOCAL;
    }
}
