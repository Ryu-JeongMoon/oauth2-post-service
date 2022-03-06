package com.support.oauth2postservice.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import com.support.oauth2postservice.service.dto.response.QMemberReadResponse;
import com.support.oauth2postservice.util.PagingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.support.oauth2postservice.domain.entity.QMember.member;
import static com.support.oauth2postservice.util.QueryDslUtils.nullSafeBuilder;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final PagingHelper pagingHelper;
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<MemberReadResponse> findActiveToResponse(String id) {
    return Optional.ofNullable(
        queryFactory.select(new QMemberReadResponse(
                member.id, member.email, member.nickname, member.role, member.status, member.latestAuthProvider)
            )
            .from(member)
            .where(member.status.eq(Status.ACTIVE).and(member.id.eq(id)))
            .fetchOne()
    );
  }

  @Override
  public Page<MemberReadResponse> search(MemberSearchRequest memberSearchRequest) {
    Pageable pageable = memberSearchRequest.getPageable();

    JPAQuery<MemberReadResponse> query = queryFactory
        .select(new QMemberReadResponse(member.id, member.email, member.nickname, member.role, member.status, member.latestAuthProvider))
        .from(member)
        .where(getConditionFrom(memberSearchRequest));

    return pagingHelper.getPage(member.getType(), query, pageable);
  }

  private BooleanBuilder getConditionFrom(MemberSearchRequest request) {
    return nullSafeBuilder(() -> member.email.contains(request.getEmail()))
        .and(nullSafeBuilder(() -> member.nickname.contains(request.getNickname())))
        .and(nullSafeBuilder(() -> member.role.eq(request.getRole())));
  }
}
