package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.util.constant.PageConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends JpaTest {

  private Post post;
  private Member member;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    memberRepository.save(member);

    post = PostTestHelper.getDefault(member);
    POST_ID = postRepository.save(post).getId();
  }

  @Test
  @DisplayName("자동 생성 UUID 확인")
  void checkGeneratedId() {
    assertThat(POST_ID).isNotNull();

    assertThat(POST_ID.length()).isEqualTo(36);
  }

  @Test
  @DisplayName("EntityListener 작동")
  void entityListener() {
    Post post = postRepository.findActive(POST_ID)
        .orElseGet(() -> Post.builder().build());

    assertThat(post.getCreatedAt()).isNotNull();
    assertThat(post.getModifiedAt()).isNotNull();
  }

  @Test
  @DisplayName("equals proxy 비교 성공")
  void equals() {
    Post postProxy = entityManager.getReference(Post.class, POST_ID);

    assertThat(post.equals(postProxy)).isTrue();
  }

  @Test
  @DisplayName("ReadResponse 형태 직접 조회")
  void findActiveToResponse() {
    boolean isPostPresent = postRepository.findActiveToResponse(POST_ID).isPresent();

    assertThat(isPostPresent).isTrue();
  }

  @Test
  @DisplayName("조건 검색 성공")
  void search() {
    PostSearchRequest searchRequest = PostSearchRequest.builder()
        .nickname(MemberTestHelper.USER_NICKNAME)
        .build();

    Page<PostReadResponse> result = postRepository.search(searchRequest);

    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("조건 검색 - 날짜 조건 검색 결과 없음")
  void searchNoResultByNotMatchedCondition() {
    PostSearchRequest searchRequest = PostSearchRequest.builder()
        .openedAt(PostTestHelper.CLOSED_AT)
        .build();

    Page<PostReadResponse> result = postRepository.search(searchRequest);

    assertThat(result.getTotalElements()).isEqualTo(0);
  }

  @Disabled("검색 조건 변경으로 인한 일시 중단")
  @Test
  @DisplayName("기본 조건으로 검색 시 게시글 오픈 날짜 내림차순 정렬")
  void searchByDefaultCondition() {
    // it goes to save 0panda -> 1panda -> 2panda, 2panda is last one
    for (int i = 0; i < 3; i++) {
      Post post = Post.builder()
          .title(i + "panda")
          .content(i + "bear")
          .member(member)
          .openedAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(i, i)))
          .closedAt(LocalDateTime.MAX)
          .build();
      postRepository.save(post);
    }

    PostSearchRequest searchRequest = PostSearchRequest.builder()
        .build();
    Page<PostReadResponse> postReadResponses = postRepository.search(searchRequest);
    PostReadResponse postReadResponse = postReadResponses.getContent().get(0);

    String lastOpenedPostTitle = "2panda";
    assertThat(postReadResponse.getTitle()).isEqualTo(lastOpenedPostTitle);
  }

  @Test
  @DisplayName("게시 일자 오름차순으로 검색 성공")
  void searchByOpenedAtAscending() {
    // given
    final String titleOfVeryOldOne = "very old one";
    LocalDateTime veryOldDay = LocalDateTime.of(1900, 1, 1, 1, 1, 1);

    Post post = Post.builder()
        .member(member)
        .title(titleOfVeryOldOne)
        .content(titleOfVeryOldOne)
        .openedAt(veryOldDay)
        .closedAt(LocalDateTime.MAX)
        .build();
    postRepository.save(post);

    // when
    PostSearchRequest searchRequest = PostSearchRequest.builder()
        .page(0)
        .size(500)
        .sorts(Collections.singletonList(Pair.of(PageConstants.Column.OPENED_AT, Sort.Direction.ASC)))
        .build();
    Page<PostReadResponse> postReadResponses = postRepository.search(searchRequest);
    PostReadResponse postReadResponse = postReadResponses.getContent().get(0);

    // then
    assertThat(postReadResponse.getTitle()).isEqualTo(titleOfVeryOldOne);
  }
}