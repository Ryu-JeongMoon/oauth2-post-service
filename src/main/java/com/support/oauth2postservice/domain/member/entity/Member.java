package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.core.exception.ExceptionMessages;
import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member_email", columnNames = {"email"})})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false, length = 320)
    private String email;

    @Size(min = 4, max = 255)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(length = 10, nullable = false, name = "login_type")
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private LocalDateTime leftAt;

    @Builder
    public Member(String name, String email, String password, LoginType loginType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.status = Status.ACTIVE;
        this.loginType = loginType != null ? loginType : LoginType.LOCAL;
    }

    public void encodePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void editInfo(String name, Role role, Status status) {
        if (StringUtils.hasText(name) && !this.name.equals(name))
            this.name = name;

        if (this.role != role && role != null)
            this.role = role;

        if (this.status != status && status != null)
            this.status = status;
    }

    public void leave() {
        if (this.leftAt != null)
            throw new IllegalStateException(ExceptionMessages.MEMBER_ALREADY_LEFT);

        this.leftAt = LocalDateTime.now();
        this.status = Status.INACTIVE;
    }
}
