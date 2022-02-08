package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends JpaTest {

    @BeforeEach
    void setUp() {
        Member member = MemberTestHelper.createUser();
        memberRepository.save(member);

        Post post = PostTestHelper.createDefault(member);
        POST_ID = postRepository.save(post).getId();
    }

    @Test
    @DisplayName("EntityListener 작동")
    void entityListener() {
        Post post = postRepository.findActive(POST_ID)
                .orElseGet(() -> null);

        assertThat(post.getCreatedAt()).isNotNull();
        assertThat(post.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("ReadResponse 형태 직접 조회")
    void findActiveToResponse() {
        PostReadResponse postReadResponse = postRepository.findActiveToResponse(POST_ID)
                .orElseGet(() -> null);

        assertThat(postReadResponse).isNotNull();
    }
}