package com.support.oauth2postservice.domain.post.entity;

import com.support.oauth2postservice.util.exception.ExceptionMessages;
import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Size(min = 1)
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

    public void delete() {
        changeStatus(Status.INACTIVE);
    }

    public void restore() {
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
