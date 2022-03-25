package com.support.oauth2postservice.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableTestHelper {

  public static Pageable createDefault() {
    return PageRequest.of(10, 10);
  }
}
