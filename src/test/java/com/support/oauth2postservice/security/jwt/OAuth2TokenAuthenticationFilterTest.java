package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.constant.UriConstants;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2TokenAuthenticationFilterTest {

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  @Test
  @DisplayName("startsWithAny 방식 보다 AntPathMatcher 방식이 더 빠르다")
  void compare() {
    String uri = "/vendor";

    long start = System.nanoTime();
    Arrays.stream(UriConstants.SHOULD_NOT_FILTER_URL_PATTERN)
        .anyMatch(prefix -> ANT_PATH_MATCHER.match(prefix, uri));
    long timeOfAntPathMatcher = System.nanoTime() - start;

    start = System.nanoTime();
    StringUtils.startsWithAny(uri, UriConstants.SHOULD_NOT_FILTER_URL_PATTERN);
    long timeOfStringUtils = System.nanoTime() - start;

    assertThat(timeOfAntPathMatcher).isLessThan(timeOfStringUtils);
  }
}