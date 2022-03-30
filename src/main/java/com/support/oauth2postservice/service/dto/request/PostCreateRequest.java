package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
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
  private String memberId;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  @NotNull
  private LocalDateTime openedAt;

  @Future
  @NotNull
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
