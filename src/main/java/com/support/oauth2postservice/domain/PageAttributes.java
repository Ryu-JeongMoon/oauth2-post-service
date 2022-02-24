package com.support.oauth2postservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PageAttributes {

  protected int page;

  protected int size;

  protected List<Pair<String, Sort.Direction>> sorts;
}
