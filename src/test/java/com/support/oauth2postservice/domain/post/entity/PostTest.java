package com.support.oauth2postservice.domain.post.entity;

import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.helper.PostTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

    private Post post;

    @BeforeEach
    void setUp() {
        post = PostTestHelper.getDefault(null);
    }

    @Test
    @DisplayName("Post 인스턴스 생성 확인")
    void post() {
        Post post = Post.builder()
                .openedAt(LocalDateTime.MIN)
                .closedAt(LocalDateTime.MAX)
                .build();

        assertThat(post).isNotNull();
    }

    @Test
    @DisplayName("Post 인스턴스 생성 실패 - 시작 시간이 종료 시간보다 늦을 수 없다")
    void postFailByLocalDateTimeRule() {
        assertThrows(IllegalArgumentException.class,
                () -> Post.builder()
                        .openedAt(LocalDateTime.MAX)
                        .closedAt(LocalDateTime.MIN)
                        .build()
        );
    }

    @Test
    @DisplayName("게시글 삭제 시 상태 변경")
    void close() {
        post.close();

        assertThat(post.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    @DisplayName("게시글 복구 시 상태 변경")
    void reopen() {
        post.reopen();

        assertThat(post.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("업데이트 소스로부터 데이터 변경")
    void editFrom() {
        Post updateSource = Post.builder()
                .title("new-title")
                .content("new-content")
                .build();

        post.editFrom(updateSource);

        assertThat(post.getTitle()).isEqualTo(updateSource.getTitle());
        assertThat(post.getContent()).isEqualTo(updateSource.getContent());
    }

    @Test
    @DisplayName("equals 비교 - 다른 제목 비교 시 False")
    void equalsFailByAnotherId() {
        Post anotherPost = Post.builder()
                .title(PostTestHelper.TITLE + 1)
                .build();

        assertThat(this.post.equals(anotherPost)).isFalse();
    }
}