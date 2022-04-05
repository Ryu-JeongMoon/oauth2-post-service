package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.util.constant.RegexpConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequest {

  @NotBlank
  @Schema(description = "회원 고유 번호", example = "d763785c-f33f-4bba-b50e-fed6a9cc41ff", minLength = 36, maxLength = 36, required = true, title = "UUID")
  private String memberId;

  @NotBlank
  @Schema(description = "제목", example = "oauth2-post-service", required = true)
  private String title;

  @NotBlank
  @Schema(description = "내용", example = "oauth2-post-service", required = true)
  private String content;

  @NotNull
  @Schema(description = "게시일", example = "2022-02-12 15:30", pattern = RegexpConstants.DATETIME, required = true)
  private LocalDateTime openedAt;

  @Future
  @NotNull
  @Schema(description = "게시 종료일", example = "2022-02-14 15:30", pattern = RegexpConstants.DATETIME, required = true)
  private LocalDateTime closedAt;

  @Builder
  public PostCreateRequest(String memberId, String title, String content, LocalDateTime openedAt, LocalDateTime closedAt) {
    this.memberId = memberId;
    this.title = title;
    this.content = content;
    this.openedAt = openedAt;
    this.closedAt = closedAt;
  }

  public static PostCreateRequest withMemberId(String memberId) {
    return PostCreateRequest.builder()
        .memberId(memberId)
        .build();
  }

  public Post toEntity(Member member) {
    return Post.builder()
        .member(member)
        .title(this.title)
        .content(this.content)
        .openedAt(this.openedAt)
        .closedAt(this.closedAt)
        .build();
  }
}
