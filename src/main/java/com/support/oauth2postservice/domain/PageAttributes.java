package com.support.oauth2postservice.domain;

import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PageAttributes {

  protected int page = PageConstants.DEFAULT_PAGE_NUMBER;

  protected int size = PageConstants.DEFAULT_PAGE_SIZE;

  protected String[] sorts = new String[]{};

  protected String[] orders = new String[]{};

  public Pageable getPageable() {
    return QPageRequest.of(getPage(), getSize(), getQSort());
  }

  protected abstract QSort getQSort();
}
