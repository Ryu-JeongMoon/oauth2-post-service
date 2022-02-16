package com.support.oauth2postservice.domain.post.entity;

import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
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
    indexes = @Index(name = "ix_title", columnList = "title"),
    uniqueConstraints = @UniqueConstraint(name = "uk_post_title", columnNames = "title")
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Post extends BaseEntity {

  @Id
  @Column(name = "post_id")
  @GeneratedValue(generator = JpaConstants.UUID2)
  @GenericGenerator(name = JpaConstants.UUID2, strategy = JpaConstants.UUID2_GENERATOR)
  private String id;

  @JoinColumn(name = "member_id")
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

  @Column(name = "opened_at", nullable = false)
  private LocalDateTime openedAt;

  @Column(name = "closed_at", nullable = false)
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

    if (StringUtils.hasText(source.getTitle()))
      this.title = source.getTitle();

    if (StringUtils.hasText(source.getContent()))
      this.content = source.getContent();

    if (source.getStatus() != null)
      this.status = source.getStatus();

    if (source.getOpenedAt() != null)
      this.openedAt = source.getOpenedAt();

    if (source.getClosedAt() != null)
      this.closedAt = source.getClosedAt();
  }
}
