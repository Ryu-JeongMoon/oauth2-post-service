package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEditRequest {

  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  private String title;

  private String content;

  private LocalDateTime openedAt;

  private LocalDateTime closedAt;

  @Builder
  public PostEditRequest(String title, String content, LocalDateTime openedAt, LocalDateTime closedAt) {
    this.title = title;
    this.content = content;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }

  public Post toEntity() {
    return Post.builder()
        .title(title)
        .content(content)
        .openedAt(openedAt)
        .closedAt(closedAt)
        .build();
  }
}
