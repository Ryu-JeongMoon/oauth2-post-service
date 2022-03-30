package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEditRequest {

  @EqualsAndHashCode.Include
  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  private String title;

  @EqualsAndHashCode.Include
  private String content;

  @EqualsAndHashCode.Include
  private LocalDateTime openedAt;

  @EqualsAndHashCode.Include
  private LocalDateTime closedAt;

  @Builder
  public PostEditRequest(String title, String content, LocalDateTime openedAt, LocalDateTime closedAt) {
    this.title = title;
    this.content = content;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }

  public static PostEditRequest empty() {
    return new PostEditRequest();
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
