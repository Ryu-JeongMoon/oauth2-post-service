package com.support.oauth2postservice.domain.post.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

    @Test
    @DisplayName("시작 시간이 종료 시간보다 늦을 수 없다")
    void openCloseComparison() {
        assertThrows(IllegalArgumentException.class, () -> Post.builder()
                .openedAt(LocalDateTime.MAX)
                .closedAt(LocalDateTime.MIN)
                .build());
    }
}