package com.support.oauth2postservice.service.post;

import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.service.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostServiceTest extends ServiceTest {

    private Post post;


    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, memberRepository);

        Member user = MemberTestHelper.createUser();
        post = PostTestHelper.createDefault(user);
    }

    @Test
    void findActivePost() {
    }

    @Test
    void write() {
    }

    @Test
    void edit() {
    }

    @Test
    void delete() {
    }

    @Test
    void restore() {
    }
}