package com.support.oauth2postservice.service.post.dto.request;

import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEditRequest {

    private String title;

    private String content;

    private Status status;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .status(status)
                .openedAt(openedAt)
                .closedAt(closedAt)
                .build();
    }
}