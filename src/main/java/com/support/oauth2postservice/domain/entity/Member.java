package com.support.oauth2postservice.domain.entity;

import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.JpaConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Member Entity 중 AuthProvider 란 인증 제공자를 의미한다<br/>
 * 애플리케이션 자체 로그인의 경우 LOCAL, 그 외 각 벤더의 이름을 나타낸다<br/><br/>
 * InitialAuthProvider - 신규 유입 통계를 확인하기 위해 초기 진입점 표현<br/>
 * LatestAuthProvider - 로그인 방식이 달라질 때 현재 인증 제공자 표현<br/>
 * 각각의 처리를 담당하는 CustomUserDetailsService, CustomOAuth2MemberService 에서 변경 된다<br/>
 * 가입 후 로그인 이력이 없다면 LatestAuthProvider 는 null 로 초기화 된다
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = ColumnConstants.Name.UNIQUE_EMAIL, columnNames = ColumnConstants.Name.EMAIL)
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
  @Column(name = ColumnConstants.Name.INITIAL_AUTH_PROVIDER, nullable = false, length = ColumnConstants.Length.DEFAULT_STRING)
  private AuthProvider initialAuthProvider;

  @Enumerated(value = EnumType.STRING)
  @Column(name = ColumnConstants.Name.LATEST_AUTH_PROVIDER, length = ColumnConstants.Length.DEFAULT_STRING)
  private AuthProvider latestAuthProvider;

  @Column(name = ColumnConstants.Name.LEFT_AT)
  private LocalDateTime leftAt;

  @Builder
  public Member(String email, String nickname, String password, Role role, AuthProvider initialAuthProvider) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.status = Status.ACTIVE;
    this.role = role != null ? role : Role.USER;
    this.initialAuthProvider = initialAuthProvider != null ? initialAuthProvider : AuthProvider.LOCAL;
  }

  /**
   * 비밀번호 암호화는 bouncy-castle 에서 제공하는 Argon2PasswordEncoder 를 사용한다<br/>
   * 암호화의 결과값에 영향을 미치는 요소는 아래와 같다<br/>
   * salt length, hash length, parallelism, memory usage, iterations<br/>
   * 기본 설정을 따르면 결과값은 96 이 나오고, 필요 시 위 요소를 변경하여<br/>
   * 더 큰 사이즈의 암호화된 비밀번호를 얻을 수 있다
   */
  public void changeToEncodedPassword(String encodedPassword) {
    if (StringUtils.isBlank(encodedPassword) || encodedPassword.length() != ColumnConstants.Length.ENCODED_PASSWORD)
      throw new IllegalArgumentException(ExceptionMessages.Member.PASSWORD_NOT_ENCODED);

    this.password = encodedPassword;
  }

  public void changeBy(String nickname, Role role, Status status) {
    if (StringUtils.isNotBlank(nickname))
      this.nickname = nickname;

    if (role != null)
      this.role = role;

    if (status != null)
      changeStatus(status);
  }

  private void changeStatus(Status status) {
    if (status == Status.ACTIVE)
      restore();
    else
      leave();
  }

  public void leave() {
    if (this.leftAt != null)
      throw new IllegalStateException(ExceptionMessages.Member.ALREADY_LEFT);

    this.leftAt = LocalDateTime.now();
    this.status = Status.INACTIVE;
  }

  public void restore() {
    this.leftAt = null;
    this.status = Status.ACTIVE;
  }

  public void changeLatestAuthProvider(AuthProvider latestAuthProvider) {
    this.latestAuthProvider = latestAuthProvider;
  }
}
