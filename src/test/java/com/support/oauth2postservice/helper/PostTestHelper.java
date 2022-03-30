package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.service.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;

import java.time.LocalDateTime;

public class PostTestHelper {

  public static final String ID = "1234567890";
  public static final String TITLE = "panda";
  public static final String CONTENT = "bear";
  public static final LocalDateTime OPENED_AT = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  public static final LocalDateTime CLOSED_AT = LocalDateTime.of(9999, 1, 1, 0, 0, 0);

  public static Post getDefault(Member member) {
    return Post.builder()
        .title(TITLE)
        .content(CONTENT)
        .member(member)
        .openedAt(OPENED_AT)
        .closedAt(CLOSED_AT)
        .build();
  }

  public static PostCreateRequest getCreateRequest(String memberId) {
    return PostCreateRequest.builder()
        .memberId(memberId)
        .title(TITLE)
        .content(CONTENT)
        .openedAt(OPENED_AT)
        .closedAt(CLOSED_AT)
        .build();
  }

  public static PostEditRequest getEditRequest() {
    return PostEditRequest.builder()
        .title(TITLE)
        .content(CONTENT)
        .openedAt(OPENED_AT)
        .closedAt(CLOSED_AT)
        .build();
  }

  public static PostSearchRequest getSearchRequest(String nickname) {
    return PostSearchRequest.builder()
        .nickname(nickname)
        .build();
  }

  public static PostReadResponse getReadResponse(Post post) {
    return PostReadResponse.builder()
        .id(ID)
        .nickname(post.getMember().getNickname())
        .title(post.getTitle())
        .content(post.getContent())
        .openedAt(post.getOpenedAt())
        .build();
  }
}
