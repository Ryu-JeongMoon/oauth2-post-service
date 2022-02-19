package com.support.oauth2postservice.domain.post.entity;

import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.JpaConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    indexes = @Index(name = ColumnConstants.Name.INDEX_TITLE, columnList = ColumnConstants.Name.TITLE),
    uniqueConstraints = @UniqueConstraint(name = ColumnConstants.Name.UNIQUE_TITLE, columnNames = ColumnConstants.Name.TITLE)
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Post extends BaseEntity {

  @Id
  @Column(name = ColumnConstants.Name.POST_ID)
  @GeneratedValue(generator = JpaConstants.UUID2)
  @GenericGenerator(name = JpaConstants.UUID2, strategy = JpaConstants.UUID2_GENERATOR)
  private String id;

  @JoinColumn(name = ColumnConstants.Name.MEMBER_ID)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Member member;

  @EqualsAndHashCode.Include
  @Column(nullable = false)
  private String title;

  @Lob
  @Column(nullable = false)
  private String content;

  @Enumerated(value = EnumType.STRING)
  private Status status;

  @Column(name = ColumnConstants.Name.OPENED_AT, nullable = false)
  private LocalDateTime openedAt;

  @Column(name = ColumnConstants.Name.CLOSED_AT, nullable = false)
  private LocalDateTime closedAt;

  @Builder
  public Post(Member member, String title, String content, Status status, LocalDateTime openedAt, LocalDateTime closedAt) {
    if (openedAt != null && closedAt != null && openedAt.isAfter(closedAt))
      throw new IllegalArgumentException(ExceptionMessages.POST_INCORRECT_DATE);

    this.member = member;
    this.title = title;
    this.content = content;
    this.status = status != null ? status : Status.ACTIVE;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }

  public void close() {
    changeStatus(Status.INACTIVE);
  }

  public void reopen() {
    changeStatus(Status.ACTIVE);
  }

  private void changeStatus(Status status) {
    this.status = status;
  }

  public void editFrom(Post source) {
    if (source.getOpenedAt() != null && source.getClosedAt() != null && source.getOpenedAt().isAfter(source.getClosedAt()))
      throw new IllegalArgumentException(ExceptionMessages.POST_INCORRECT_DATE);

    this.openedAt = source.getOpenedAt();
    this.closedAt = source.getClosedAt();

    if (StringUtils.hasText(source.getTitle()))
      this.title = source.getTitle();

    if (StringUtils.hasText(source.getContent()))
      this.content = source.getContent();

    if (source.getStatus() != null)
      this.status = source.getStatus();
  }
}
