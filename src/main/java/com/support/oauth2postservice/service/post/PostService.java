package com.support.oauth2postservice.service.post;

import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.domain.post.repository.PostRepository;
import com.support.oauth2postservice.service.post.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.post.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public Page<PostReadResponse> searchByCondition(PostSearchRequest condition, Pageable pageable) {
    return postRepository.search(condition, pageable);
  }

  @Transactional(readOnly = true)
  public PostReadResponse findActivePost(String postId) {
    return postRepository.findActiveToResponse(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.POST_NOT_FOUND));
  }

  @Transactional
  public void write(PostCreateRequest postCreateRequest) {
    Member member = memberRepository.findActiveByNickname(postCreateRequest.getNickname())
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.MEMBER_NOT_FOUND));

    Post post = postCreateRequest.toEntity(member);
    postRepository.save(post);
  }

  @Transactional
  public void edit(String postId, PostEditRequest postEditRequest) {
    if (postEditRequest == null)
      return;

    Post post = postRepository.findActive(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.POST_NOT_FOUND));

    Post updateSource = postEditRequest.toEntity();
    post.editFrom(updateSource);
  }

  @Transactional
  public void close(String postId) {
    postRepository.findActive(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.POST_NOT_FOUND))
        .close();
  }

  @Transactional
  public void reopen(String postId) {
    postRepository.findInactive(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.POST_NOT_FOUND))
        .reopen();
  }
}
