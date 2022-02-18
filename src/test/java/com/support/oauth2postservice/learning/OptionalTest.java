package com.support.oauth2postservice.learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OptionalTest {

  private final List<String> names = Arrays.asList("panda", "bear", "tiger", "lion", "chicken");

  @Test
  @DisplayName("orElse - 무조건 평가")
  void orElseTest() {
    String name = Optional.of("orElse be evaluated when Optional is not empty")
        .orElse(getRandomName());
  }

  @Test
  @DisplayName("orElseGet - Optional 빈 값일 때만 평가")
  void orElseGetTest() {
    String name = Optional.of("orElse be evaluated when Optional is not empty")
        .orElseGet(this::getRandomName);
  }

  private String getRandomName() {
    System.out.println("getRandomName() method - start");

    Random random = new Random();
    int index = random.nextInt(5);

    System.out.println("getRandomName() method - end");
    return names.get(index);
  }

  @Test
  @DisplayName("LocalDateTime.MAX & MIN DB 날짜 검색 시 깨짐 주의")
  void compareLocalDateTime() {
    LocalDateTime max = LocalDateTime.MAX;
    LocalDateTime min = LocalDateTime.MIN;
    LocalDateTime now = LocalDateTime.now();

    Assertions.assertThat(now).isBefore(max);
    Assertions.assertThat(now).isAfter(min);
  }
}
