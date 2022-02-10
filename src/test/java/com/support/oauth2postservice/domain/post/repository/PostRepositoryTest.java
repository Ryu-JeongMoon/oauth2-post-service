package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("ReadResponse 형태 직접 조회")
    void findActiveToResponse() {
        boolean isPostPresent = postRepository.findActiveToResponse(POST_ID).isPresent();

        assertThat(isPostPresent).isTrue();
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
}