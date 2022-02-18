package com.support.oauth2postservice.util.constant;

import org.springframework.data.domain.Sort;

public class PageConstants {

  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

  public static class Column  {
    public static final String OPENED_AT = "openedAt";
  }
}
