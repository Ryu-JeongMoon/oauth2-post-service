package com.support.oauth2postservice.util;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PagingHelper {

  private final EntityManager entityManager;

  private <T> Querydsl getQuerydsl(Class<?> targetClass) {
    PathBuilder<?> builder = new PathBuilderFactory().create(targetClass);
    return new Querydsl(entityManager, builder);
  }

  public <T> Page<T> getPage(Class<?> entityType, JPQLQuery<T> query, Pageable pageable) {
    long totalCount = query.fetch().size();
    List<T> results = getQuerydsl(entityType).applyPagination(pageable, query).fetch();
    return new PageImpl<>(results, pageable, totalCount);
  }
}
