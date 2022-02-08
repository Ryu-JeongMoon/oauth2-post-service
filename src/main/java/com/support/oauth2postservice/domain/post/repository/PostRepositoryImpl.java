package com.support.oauth2postservice.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.service.post.dto.response.QPostReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.support.oauth2postservice.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostReadResponse> findActiveToResponse(Long id) {
        return Optional.ofNullable(
                queryFactory.select(new QPostReadResponse(post.id, post.member.nickname, post.title, post.content, post.createdAt))
                        .from(post)
                        .join(post.member)
                        .where(post.status.eq(Status.ACTIVE))
                        .fetchOne()
        );
    }
}
