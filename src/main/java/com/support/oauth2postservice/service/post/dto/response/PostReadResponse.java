package com.support.oauth2postservice.service.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadResponse {

    private Long id;

    private String nickname;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    @QueryProjection
    public PostReadResponse(Long id, String nickname, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
