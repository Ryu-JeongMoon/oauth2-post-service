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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryDslUtils {

  public static BooleanBuilder nullSafeBuilder(Supplier<? extends BooleanExpression> supplier) {
    try {
      return new BooleanBuilder(supplier.get());
    } catch (IllegalArgumentException | NullPointerException e) {
      return new BooleanBuilder();
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked", "cast"})
  public static OrderSpecifier<?>[] getSortedColumn(Sort sorts, Path<?> parent) {
    return sorts.stream()
        .map(sort -> {
          Order direction = sort.getDirection().isAscending() ? Order.ASC : Order.DESC;
          SimplePath<Object> filedPath = Expressions.path(Object.class, parent, sort.getProperty());
          return new OrderSpecifier(direction, filedPath);
        })
        .toArray(OrderSpecifier[]::new);
  }

  @SuppressWarnings(value = {"rawtypes", "unchecked", "cast"})
  public static QSort getQSort(List<Pair<String, Sort.Direction>> sorts, Path<?> parent, String[] keywords) {
    return sorts.stream()
        .filter(sort -> StringUtils.equalsAnyIgnoreCase(sort.getLeft(), keywords))
        .map(pair -> {
          Order order = pair.getRight().isAscending() ? Order.ASC : Order.DESC;
          SimplePath<Object> fieldPath = Expressions.path(Object.class, parent, pair.getLeft());
          return new OrderSpecifier(order, fieldPath);
        })
        .collect(collectingAndThen(toList(), orderSpecifiers -> QSort.by(orderSpecifiers.toArray(new OrderSpecifier[]{}))));
  }
}
