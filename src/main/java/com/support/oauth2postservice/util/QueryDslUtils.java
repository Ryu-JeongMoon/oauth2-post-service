package com.support.oauth2postservice.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryDslUtils {

  public static BooleanBuilder nullSafeBuilder(Supplier<? extends BooleanExpression> supplier) {
    try {
      return new BooleanBuilder(supplier.get());
    } catch (IllegalArgumentException | NullPointerException e) {
      return new BooleanBuilder();
    }
  }

  public static OrderSpecifier<?>[] getSortedColumn(Sort sorts, Path<?> parent) {
    return sorts.stream()
        .map(sort -> {
          Order direction = sort.getDirection().isAscending() ? Order.ASC : Order.DESC;
          SimplePath<Object> filedPath = Expressions.path(Object.class, parent, sort.getProperty());
          return new OrderSpecifier(direction, filedPath);
        })
        .toArray(OrderSpecifier[]::new);
  }
}
