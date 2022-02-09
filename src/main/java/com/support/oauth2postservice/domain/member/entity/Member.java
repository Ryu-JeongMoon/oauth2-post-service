package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.util.exception.ExceptionMessages;
import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_member_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_member_nickname", columnNames = "nickname")
})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(length = 20, nullable = false)
    private String nickname;

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
    public Member(String email, String nickname, String password, Role role, LoginType loginType) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.status = Status.ACTIVE;
        this.role = role != null ? role : Role.USER;
        this.loginType = loginType != null ? loginType : LoginType.LOCAL;
    }

    public void encodePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void editInfo(String nickname, Role role, Status status) {
        if (StringUtils.hasText(nickname) && !this.nickname.equals(nickname))
            this.nickname = nickname;

        if (!this.role.equals(role) && role != null)
            this.changeRole(role);

        if (!this.status.equals(status) && status != null)
            this.status = status;
    }

    public void leave() {
        if (this.leftAt != null)
            throw new IllegalStateException(ExceptionMessages.MEMBER_ALREADY_LEFT);

        this.leftAt = LocalDateTime.now();
        this.status = Status.INACTIVE;
    }

    public void changeRole(Role role) {
        if (this.role.equals(Role.USER) || this.role.equals(Role.MANAGER) && role.equals(Role.ADMIN))
            throw new AccessDeniedException(ExceptionMessages.MEMBER_ACCESS_DENIED);

        this.role = role;
    }
}
