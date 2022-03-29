package com.support.oauth2postservice.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2TokenAuthenticationFilterTest {

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  public static final String[] SHOULD_NOT_FILTER_URL_PATTERN_ARRAY = {
      "/css/**", "/js/**", "/img/**", "/vendor/**", "/logout/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/**"
  };
  public static final List<String> SHOULD_NOT_FILTER_URL_PATTERN_LIST = Arrays.asList(SHOULD_NOT_FILTER_URL_PATTERN_ARRAY);

  @Test
  @DisplayName("경로 비교 속도 실험 - startsWithAny << AntPathMatcher & Array << List")
  void compare() {
    String uri = "/vendor";

    long start = System.nanoTime();
    Arrays.stream(SHOULD_NOT_FILTER_URL_PATTERN_ARRAY)
        .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, uri));
    long timeOfPatternArrayWithAntPathMatcher = System.nanoTime() - start;

    start = System.nanoTime();
    SHOULD_NOT_FILTER_URL_PATTERN_LIST.stream()
        .anyMatch(pattern -> ANT_PATH_MATCHER.match(pattern, uri));
    long timeOfPatternListWithAntPathMatcher = System.nanoTime() - start;

    start = System.nanoTime();
    StringUtils.startsWithAny(uri, SHOULD_NOT_FILTER_URL_PATTERN_ARRAY);
    long timeOfStringUtils = System.nanoTime() - start;

    assertThat(timeOfPatternListWithAntPathMatcher).isLessThan(timeOfPatternArrayWithAntPathMatcher);
    assertThat(timeOfPatternArrayWithAntPathMatcher).isLessThan(timeOfStringUtils);

    System.out.println("timeOfStringUtils = " + timeOfStringUtils);
    System.out.println("timeOfPatternListWithAntPathMatcher = " + timeOfPatternListWithAntPathMatcher);
    System.out.println("timeOfPatternArrayWithAntPathMatcher = " + timeOfPatternArrayWithAntPathMatcher);
  }
}