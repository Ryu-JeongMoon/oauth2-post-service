package com.support.oauth2postservice.domain.post.entity;

import com.support.oauth2postservice.core.exception.ExceptionMessages;
import com.support.oauth2postservice.domain.BaseEntity;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public Post(Member member, String title, String content, LocalDateTime openedAt, LocalDateTime closedAt) {
        if (openedAt.isAfter(closedAt))
            throw new IllegalArgumentException(ExceptionMessages.POST_INCORRECT_DATE);

        this.member = member;
        this.title = title;
        this.content = content;
        this.status = Status.ACTIVE;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }
}
