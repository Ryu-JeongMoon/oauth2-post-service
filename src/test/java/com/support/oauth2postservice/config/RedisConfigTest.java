package com.support.oauth2postservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
class RedisConfigTest {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  @DisplayName("strings 값 테스트 성공")
  void testStrings() {
    // given
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    String key = "string-key";

    // when
    valueOperations.set(key, "panda");

    // then
    String value = valueOperations.get(key);
    assertThat(value).isEqualTo("panda");
  }


  @Test
  @DisplayName("set 값 테스트 성공")
  void testSet() {
    SetOperations<String, String> setOperations = redisTemplate.opsForSet();
    String key = "set-key";

    setOperations.add(key, "h", "e", "l", "l", "o");

    Set<String> members = setOperations.members(key);
    Long size = setOperations.size(key);

    assertThat(members).containsOnly("h", "e", "l", "o");
    assertThat(size).isEqualTo(4);
  }

  @Test
  @DisplayName("hash 값 테스트 성공")
  void testHash() {
    HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
    String key = "hash-key";

    hashOperations.put(key, "hello", "world");

    Object value = hashOperations.get(key, "hello");
    assertThat(value).isEqualTo("world");

    Map<Object, Object> entries = hashOperations.entries(key);
    assertThat(entries.keySet()).containsExactly("hello");
    assertThat(entries.values()).containsExactly("world");

    Long size = hashOperations.size(key);
    assertThat(size).isEqualTo(entries.size());
  }
}