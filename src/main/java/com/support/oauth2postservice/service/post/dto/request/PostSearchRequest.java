package com.support.oauth2postservice.service.post.dto.request;

import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchRequest {

    private String nickname;

    private String title;

    private String content;

    private Status status;

    private LocalDateTime openedAt;

    @Builder
    public PostSearchRequest(String nickname, String title, String content, Status status, LocalDateTime openedAt) {
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.status = status;
        this.openedAt = openedAt;
    }
}
