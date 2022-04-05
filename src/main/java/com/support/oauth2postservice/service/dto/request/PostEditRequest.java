package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.RegexpConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEditRequest {

  @EqualsAndHashCode.Include
  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  @Schema(description = "제목", example = "oauth2-post-service")
  private String title;

  @EqualsAndHashCode.Include
  @Schema(description = "내용", example = "oauth2-post-service")
  private String content;

  @EqualsAndHashCode.Include
  @Schema(description = "게시일", example = "2022-02-12 15:30", pattern = RegexpConstants.DATETIME)
  private LocalDateTime openedAt;

  @EqualsAndHashCode.Include
  @Schema(description = "게시 종료일", example = "2022-02-14 15:30", pattern = RegexpConstants.DATETIME)
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
