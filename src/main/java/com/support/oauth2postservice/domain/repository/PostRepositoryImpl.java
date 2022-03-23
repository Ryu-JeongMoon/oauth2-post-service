package com.support.oauth2postservice.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import com.support.oauth2postservice.service.dto.response.QPostReadResponse;
import com.support.oauth2postservice.util.PagingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.support.oauth2postservice.domain.entity.QMember.member;
import static com.support.oauth2postservice.domain.entity.QPost.post;
import static com.support.oauth2postservice.util.QueryDslUtils.nullSafeBuilder;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

  private final PagingHelper pagingHelper;
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<PostReadResponse> findResponseById(String id) {
    return Optional.ofNullable(
        queryFactory.select(new QPostReadResponse(
                post.id, post.member.nickname, post.title, post.content, post.openedAt, post.modifiedAt, post.status
            ))
            .from(post)
            .leftJoin(post.member, member)
            .where(post.id.eq(id))
            .distinct()
            .fetchOne()
    );
  }

  @Override
  public Page<PostReadResponse> search(PostSearchRequest postSearchRequest) {
    Pageable pageable = postSearchRequest.getPageable();

    JPQLQuery<PostReadResponse> query = queryFactory
        .select(new QPostReadResponse(
            post.id, post.member.nickname, post.title, post.content, post.openedAt, post.modifiedAt, post.status
        ))
        .from(post)
        .join(post.member)
        .where(getConditionFrom(postSearchRequest));

    return pagingHelper.getPage(post.getType(), query, pageable);
  }

  private BooleanBuilder getConditionFrom(PostSearchRequest request) {
    return nullSafeBuilder(() -> post.member.nickname.contains(request.getNickname()))
        .and(nullSafeBuilder(() -> post.title.contains(request.getTitle())))
        .and(nullSafeBuilder(() -> post.content.contains(request.getContent())))
        .and(nullSafeBuilder(() -> post.status.eq(request.getStatus())))
        .and(nullSafeBuilder(() -> post.openedAt.before(request.getOpenedAt())));
  }
}
