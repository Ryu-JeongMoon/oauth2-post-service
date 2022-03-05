package com.support.oauth2postservice.domain;

import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PageAttributes {

  protected int page = 0;

  protected int size = 10;

  protected List<Pair<String, Sort.Direction>> sorts;

  protected Pageable toPageable(int page, int size, List<Pair<String, Sort.Direction>> sorts) {
    if (size == 0)
      size = PageConstants.DEFAULT_PAGE_SIZE;

    Sort sort = Sort.unsorted();
    if (!CollectionUtils.isEmpty(sorts)) {
      sort = sorts.stream()
          .filter(Objects::nonNull)
          .map(pair -> new Sort.Order(pair.getRight(), pair.getLeft()))
          .collect(collectingAndThen(toList(), Sort::by));
    }

    return PageRequest.of(page, size, sort);
  }
}
