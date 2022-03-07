package com.support.oauth2postservice.service.post;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.ServiceTest;
import com.support.oauth2postservice.service.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest extends ServiceTest {

  private Member member;
  private Post post;
  private PostReadResponse postReadResponse;

  @BeforeEach
  void setUp() {
    postService = new PostService(postRepository, memberRepository);

    member = MemberTestHelper.createUser();
    post = PostTestHelper.getDefault(member);
    postReadResponse = PostTestHelper.getReadResponse(post);
  }

  @Test
  @DisplayName("검색 성공")
  void searchByCondition() {
    PostSearchRequest searchRequest = PostTestHelper.getSearchRequest(member.getNickname());

    when(postRepository.search(any()))
        .thenReturn(new PageImpl<>(Collections.singletonList(postReadResponse)));

    Page<PostReadResponse> postReadResponses = postService.searchByCondition(searchRequest);

    assertThat(postReadResponses.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("페이지 검색 사이즈 0이라면 기본 설정 사이즈로 반환")
  void searchByCondition_returnByDefaultPageSize() {
    assertDoesNotThrow(
        () -> postService.searchByCondition(
            PostSearchRequest.builder().build())
    );
  }

  @Test
  @DisplayName("조회 성공")
  void findActivePost() {
    when(postRepository.findActiveToResponse(PostTestHelper.ID))
        .thenReturn(Optional.of(postReadResponse));

    PostReadResponse result = postService.findActivePost(PostTestHelper.ID);

    assertThat(result.getTitle()).isEqualTo(post.getTitle());

    verify(postRepository, times(1))
        .findActiveToResponse(PostTestHelper.ID);
  }

  @Test
  @DisplayName("조회 실패 - 잘못된 ID")
  void findActivePost_failByWrongId() {
    String nonExistId = "NO";

    when(postRepository.findActiveToResponse(nonExistId))
        .thenThrow(IllegalArgumentException.class);

    assertThrows(IllegalArgumentException.class,
        () -> postService.findActivePost(nonExistId));

    verify(postRepository, times(1))
        .findActiveToResponse(nonExistId);
  }

  @Test
  @DisplayName("작성 성공")
  void write() {
    PostCreateRequest createRequest = PostTestHelper.getCreateRequest("panda");

    when(memberRepository.findActiveByNickname(createRequest.getNickname()))
        .thenReturn(Optional.of(member));

    postService.write(createRequest);

    Post post = createRequest.toEntity(member);
    verify(postRepository, times(1))
        .save(post);
  }

  @Test
  @DisplayName("작성 실패 - 존재하지 않는 회원 이름")
  void write_failByNonExistMember() {
    PostCreateRequest createRequest = PostTestHelper.getCreateRequest("panda");

    assertThrows(IllegalArgumentException.class,
        () -> postService.write(createRequest));
  }

  @Test
  @DisplayName("수정 성공")
  void edit() {
    PostEditRequest editRequest = PostTestHelper.getEditRequest(Status.INACTIVE);
    Post updateSource = editRequest.toEntity();
    Post mockedPost = mock(Post.class);

    when(postRepository.findActive(PostTestHelper.ID))
        .thenReturn(Optional.of(mockedPost));

    postService.edit(PostTestHelper.ID, editRequest);

    verify(mockedPost, times(1))
        .changeFrom(updateSource);
  }

  @Test
  @DisplayName("수정 실패 - 존재하지 않는 게시글 번호")
  void edit_failByNonExistPost() {
    PostEditRequest editRequest = PostTestHelper.getEditRequest(Status.INACTIVE);

    assertThrows(IllegalArgumentException.class,
        () -> postService.edit(PostTestHelper.ID, editRequest));
  }

  @Test
  @DisplayName("삭제 성공")
  void delete() {
    Post mockedPost = mock(Post.class);

    when(postRepository.findActive(any()))
        .thenReturn(Optional.of(mockedPost));

    postService.close(PostTestHelper.ID);

    verify(mockedPost, times(1))
        .close();
  }

  @Test
  @DisplayName("삭제 실패 - 존재하지 않는 게시글 번호")
  void delete_failByNonExistPost() {
    assertThrows(IllegalArgumentException.class,
        () -> postService.close(PostTestHelper.ID));
  }

  @Test
  @DisplayName("복구 성공")
  void reopen() {
    Post mockedPost = mock(Post.class);

    when(postRepository.findInactive(any()))
        .thenReturn(Optional.of(mockedPost));

    postService.reopen(PostTestHelper.ID);

    verify(mockedPost, times(1))
        .reopen();
  }

  @Test
  @DisplayName("복구 실패 - 존재하지 않는 비활성 게시글")
  void reopen_failByNonExistPost() {
    Post mockedPost = mock(Post.class);

    when(postRepository.findInactive(PostTestHelper.ID))
        .thenThrow(IllegalArgumentException.class);

    assertThrows(IllegalArgumentException.class,
        () -> postService.reopen(PostTestHelper.ID));

    verify(mockedPost, times(0))
        .reopen();
  }
}