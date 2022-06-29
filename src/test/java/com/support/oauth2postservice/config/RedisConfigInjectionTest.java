package com.support.oauth2postservice.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { RedisConfig.class })
class RedisConfigInjectionTest {

  @Autowired
  RedisConfig redisConfig;

  @Test
  @DisplayName("properties injection 테스트")
  void inject() {
    // given
    int port = redisConfig.getRedisProperties().getPort();
    String host = redisConfig.getRedisProperties().getHost();

    log.info("port = {}", port);
    log.info("host = {}", host);

    // then
    assertAll(
      () -> assertThat(port).isNotNull(),
      () -> assertThat(host).isNotNull()
    );
  }
}