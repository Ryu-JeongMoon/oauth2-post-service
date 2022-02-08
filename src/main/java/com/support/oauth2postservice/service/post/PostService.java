package com.support.oauth2postservice.service.post;

import com.support.oauth2postservice.core.exception.ExceptionMessages;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.domain.post.repository.PostRepository;
import com.support.oauth2postservice.service.post.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.post.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PostReadResponse findActivePost(Long postId) {
        return postRepository.findActiveToResponse(postId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.POST_NOT_FOUND));
    }

    @Transactional
    public void write(PostCreateRequest postCreateRequest) {
        Member member = memberRepository.findActiveByNickname(postCreateRequest.getNickname())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.MEMBER_NOT_FOUND));

        Post post = postCreateRequest.toEntity(member);
        postRepository.save(post);
    }

    @Transactional
    public void edit(Long postId, PostEditRequest postEditRequest) {
        if (postEditRequest == null)
            return;

        Post post = postRepository.findActive(postId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.POST_NOT_FOUND));

        Post updateSource = postEditRequest.toEntity();
        post.editFrom(updateSource);
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.findActive(postId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.POST_NOT_FOUND))
                .delete();
    }

    @Transactional
    public void restore(Long postId) {
        postRepository.findActive(postId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.POST_NOT_FOUND))
                .restore();
    }
}
