package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends JpaTest {

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberTestHelper.createUser();
        memberRepository.save(member);

        post = PostTestHelper.createDefault(member);
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
    @DisplayName("equals 비교 - 다른 아이디 비교 시 False")
    void equalsFailByAnotherId() {
        Post anotherPost = PostTestHelper.createDefault(member);
        postRepository.save(anotherPost);

        assertThat(post.equals(anotherPost)).isFalse();
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

        Page<PostReadResponse> result = postRepository.search(searchRequest, Pageable.ofSize(Integer.MAX_VALUE));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("조건 검색 실패 - 검색 결과 없음")
    void searchNoResultByNotMatchedCondition() {
        PostSearchRequest searchRequest = PostSearchRequest.builder()
                .openedAt(LocalDateTime.MIN)
                .build();

        Page<PostReadResponse> result = postRepository.search(searchRequest, Pageable.ofSize(Integer.MAX_VALUE));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }


}