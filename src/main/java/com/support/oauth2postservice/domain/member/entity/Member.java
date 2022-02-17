package com.support.oauth2postservice.domain.member.entity;

import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.JpaConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = ColumnConstants.Name.UNIQUE_EMAIL, columnNames = ColumnConstants.Name.EMAIL),
    @UniqueConstraint(name = ColumnConstants.Name.UNIQUE_NICKNAME, columnNames = ColumnConstants.Name.NICKNAME)
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Member extends BaseEntity {

  @Id
  @Column(name = ColumnConstants.Name.MEMBER_ID, length = ColumnConstants.Length.ID)
  @GeneratedValue(generator = JpaConstants.UUID2)
  @GenericGenerator(name = JpaConstants.UUID2, strategy = JpaConstants.UUID2_GENERATOR)
  private String id;

  @EqualsAndHashCode.Include
  @Column(nullable = false, length = ColumnConstants.Length.EMAIL)
  private String email;

  @EqualsAndHashCode.Include
  @Column(name = ColumnConstants.Name.NICKNAME, nullable = false, length = ColumnConstants.Length.NICKNAME)
  private String nickname;

  @Size(min = ColumnConstants.Length.PASSWORD_MIN, max = ColumnConstants.Length.DEFAULT_MAX)
  @Column(nullable = false, length = ColumnConstants.Length.ENCODED_PASSWORD)
  private String password;

  @Column(nullable = false, length = ColumnConstants.Length.DEFAULT_STRING)
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = ColumnConstants.Length.DEFAULT_STRING)
  private Status status;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "login_type", nullable = false, length = ColumnConstants.Length.DEFAULT_STRING)
  private LoginType loginType;

  @Column(name = ColumnConstants.Name.LEFT_AT)
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

  /**
   * 비밀번호 암호화는 bouncy-castle 에서 제공하는 Argon2PasswordEncoder 를 사용한다<br/>
   * 암호화의 결과값에 영향을 미치는 요소는 아래와 같다<br/>
   * salt length, hash length, parallelism, memory usage, iterations<br/>
   * 기본 설정을 따르면 결과값은 96 이 나오고, 필요 시 위 요소를 변경하여<br/>
   * 더 큰 사이즈의 암호화된 비밀번호를 얻을 수 있다
   */
  public void putEncodedPassword(String encodedPassword) {
    if (encodedPassword.length() != ColumnConstants.Length.ENCODED_PASSWORD)
      throw new IllegalArgumentException(ExceptionMessages.PASSWORD_NOT_ENCODED);

    this.password = encodedPassword;
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
