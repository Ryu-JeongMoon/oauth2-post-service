package com.support.oauth2postservice.service.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadResponse {

  private String id;

  private String nickname;

  private String title;

  private String content;

  private LocalDateTime openedAt;

  @Builder
  @QueryProjection
  public PostReadResponse(String id, String nickname, String title, String content, LocalDateTime openedAt) {
    this.id = id;
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.openedAt = openedAt;
  }
}
