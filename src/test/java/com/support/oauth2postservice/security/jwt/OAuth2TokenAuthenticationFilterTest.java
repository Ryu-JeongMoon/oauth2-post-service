package com.support.oauth2postservice.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PatternMatchUtils;

import java.util.Arrays;
import java.util.List;

class OAuth2TokenAuthenticationFilterTest {

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  public static final String[] SHOULD_NOT_FILTER_URL_PATTERN_ARRAY = {
      "/css/**", "/js/**", "/img/**", "/vendor/**", "/logout/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/**"
  };
  public static final List<String> SHOULD_NOT_FILTER_URL_PATTERN_LIST = Arrays.asList(SHOULD_NOT_FILTER_URL_PATTERN_ARRAY);

  /**
   * 첫 호출 시에는 아래와 같은 성능 차이를 보이지만<br/>
   * ListWithAntPathMatcher >> PatternMatchUtils >> ArrayWithAntPathMatcher >> StringUtils<br/>
   * 캐싱 될수록 다음과 같은 성능 차이를 보이게 된다 <br/>
   * PatternMatchUtils >> StringUtils >> ListWithAntPathMatcher >> ArrayWithAntPathMatcher <br/>
   * 따라서 본 프로그램에는 속도가 가장 빠른 PatternMatchUtils 를 사용한다
   */
  @RepeatedTest(5)
  @EnabledOnOs(OS.MAC)
  @DisplayName("경로 비교 속도 실험")
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

    start = System.nanoTime();
    PatternMatchUtils.simpleMatch(SHOULD_NOT_FILTER_URL_PATTERN_ARRAY, uri);
    long timeOfPatternMatchUtils = System.nanoTime() - start;

    System.out.println("timeOfStringUtils = " + timeOfStringUtils);
    System.out.println("timeOfPatternListWithAntPathMatcher = " + timeOfPatternListWithAntPathMatcher);
    System.out.println("timeOfPatternArrayWithAntPathMatcher = " + timeOfPatternArrayWithAntPathMatcher);
    System.out.println("timeOfPatternMatchUtils = " + timeOfPatternMatchUtils);
  }
}