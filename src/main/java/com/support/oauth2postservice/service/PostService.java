package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.PostRepository;
import com.support.oauth2postservice.service.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public Page<PostReadResponse> searchByCondition(PostSearchRequest condition, Role roleFromCurrentUser) {
    if (roleFromCurrentUser.isInferiorThan(Role.MANAGER))
      condition.setDefaultOptionsForUser();

    return postRepository.search(condition);
  }

  @Transactional(readOnly = true)
  public PostReadResponse findById(String postId) {
    return postRepository.findResponseById(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Post.NOT_FOUND));
  }

  @Transactional
  public void write(PostCreateRequest postCreateRequest) {
    Member member = memberRepository.findActiveById(postCreateRequest.getMemberId())
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    Post post = postCreateRequest.toEntity(member);
    postRepository.save(post);
  }

  @Transactional
  public void edit(String postId, PostEditRequest postEditRequest) {
    if (postEditRequest == null || PostEditRequest.empty().equals(postEditRequest))
      return;

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Post.NOT_FOUND));

    Post updateSource = postEditRequest.toEntity();
    post.changeFrom(updateSource);
  }

  @Transactional
  public void close(String postId) {
    postRepository.findActive(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Post.NOT_FOUND))
        .close();
  }

  @Transactional
  public void reopen(String postId) {
    postRepository.findInactive(postId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Post.NOT_FOUND))
        .reopen();
  }
}
