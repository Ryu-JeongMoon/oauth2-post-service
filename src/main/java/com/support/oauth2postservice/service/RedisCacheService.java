package com.support.oauth2postservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
public class RedisCacheService implements CacheService {

  private final ObjectMapper objectMapper;
  private final StringRedisTemplate stringRedisTemplate;
  private final ValueOperations<String, String> stringValueOperations;

  public RedisCacheService(ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
    this.objectMapper = objectMapper;
    this.stringRedisTemplate = stringRedisTemplate;
    this.stringValueOperations = stringRedisTemplate.opsForValue();
  }

  @Override
  public String getData(String key) {
    return stringValueOperations.get(key);
  }

  @Override
  public int getDataAsInt(String key) {
    return Integer.parseInt(Objects.requireNonNull(stringValueOperations.get(key)));
  }

  @Override
  public void setData(String key, Object value) {
    stringValueOperations.set(key, String.valueOf(value));
  }

  @Override
  public void setDataAndExpiration(String key, Object value, long duration) {
    Duration expireDuration = Duration.ofSeconds(duration);
    stringValueOperations.set(key, String.valueOf(value), expireDuration);
  }

  @Override
  public void deleteData(String key) {
    stringRedisTemplate.delete(key);
  }

  @Override
  public void increment(String key) {
    stringValueOperations.increment(key);
  }

  @Override
  public <T> T getObjectData(String key, Class<T> t) {
    try {
      return objectMapper.readValue(stringValueOperations.get(key), new TypeReference<T>() {
      });
    } catch (JsonProcessingException e) {
      log.info("[ERROR] :: INVALID OR NON-EXIST CACHE KEY => {} ", key);
      return null;
    }
  }
}
