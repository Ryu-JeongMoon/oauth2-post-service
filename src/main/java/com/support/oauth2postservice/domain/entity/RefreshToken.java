package com.support.oauth2postservice.domain.entity;

import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.JpaConstants;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RefreshToken extends BaseEntity {

  @Id
  @Column(name = ColumnConstants.Name.TOKEN_ID, length = ColumnConstants.Length.ID)
  @GeneratedValue(generator = JpaConstants.UUID2)
  @GenericGenerator(name = JpaConstants.UUID2, strategy = JpaConstants.UUID2_GENERATOR)
  private String id;

  @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
  @JoinColumn(name = ColumnConstants.Name.MEMBER_ID)
  private Member member;

  @EqualsAndHashCode.Include
  @Column(name = ColumnConstants.Name.TOKEN_VALUE)
  private String tokenValue;

  @Builder
  public RefreshToken(Member member, String tokenValue) {
    this.member = member;
    this.tokenValue = tokenValue;
  }
}
