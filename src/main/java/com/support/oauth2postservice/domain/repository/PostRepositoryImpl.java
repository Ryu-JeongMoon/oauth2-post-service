package com.support.oauth2postservice.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.service.post.dto.response.QPostReadResponse;
import com.support.oauth2postservice.util.PagingHelper;
import com.support.oauth2postservice.util.QueryDslUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Supplier;

import static com.support.oauth2postservice.domain.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

  private final PagingHelper pagingHelper;
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<PostReadResponse> findActiveToResponse(String id) {
    return Optional.ofNullable(
        queryFactory.select(new QPostReadResponse(post.id, post.member.nickname, post.title, post.content, post.openedAt))
            .from(post)
            .join(post.member)
            .where(post.status.eq(Status.ACTIVE).and(post.id.eq(id)))
            .fetchOne()
    );
  }

  @Override
  public Page<PostReadResponse> search(PostSearchRequest postSearchRequest) {
    Pageable pageable = postSearchRequest.getPageable();

    JPQLQuery<PostReadResponse> query = queryFactory
        .select(new QPostReadResponse(post.id, post.member.nickname, post.title, post.content, post.openedAt))
        .from(post)
        .join(post.member)
        .where(getConditionFrom(postSearchRequest));

    return pagingHelper.getPage(post.getType(), query, pageable);
  }

  private BooleanBuilder getConditionFrom(PostSearchRequest request) {
    return compareSafely(() -> post.member.nickname.contains(request.getNickname()))
        .and(compareSafely(() -> post.title.contains(request.getTitle())))
        .and(compareSafely(() -> post.content.contains(request.getContent())))
        .and(compareSafely(() -> post.status.eq(request.getStatus())))
        .and(compareSafely(() -> post.openedAt.after(request.getOpenedAt())));
  }

  private BooleanBuilder compareSafely(Supplier<? extends BooleanExpression> expressionSupplier) {
    return QueryDslUtils.nullSafeBuilder(expressionSupplier);
  }

  private OrderSpecifier<?>[] getSortedColumnInPost(Sort sorts) {
    return QueryDslUtils.getSortedColumn(sorts, post);
  }
}
