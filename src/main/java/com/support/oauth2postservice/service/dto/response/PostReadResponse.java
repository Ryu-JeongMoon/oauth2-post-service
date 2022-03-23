package com.support.oauth2postservice.service.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.support.oauth2postservice.domain.enumeration.Status;
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

  private LocalDateTime modifiedAt;

  private Status status;

  @Builder
  @QueryProjection
  public PostReadResponse(String id, String nickname, String title, String content,
                          LocalDateTime openedAt, LocalDateTime modifiedAt, Status status) {
    this.id = id;
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.openedAt = openedAt;
    this.modifiedAt = modifiedAt;
    this.status = status;
  }
}
