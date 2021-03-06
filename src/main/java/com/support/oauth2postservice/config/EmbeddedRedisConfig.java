package com.support.oauth2postservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Configuration
@Profile("test")
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class EmbeddedRedisConfig {

  private final RedisProperties redisProperties;

  private RedisServer redisServer;

  @PostConstruct
  public void redisServer() throws IOException {
    int port = isRedisRunning() ? findAvailablePort() : redisProperties.getPort();
    redisServer = new RedisServer(port);
    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
    if (redisServer != null)
      redisServer.stop();
  }

  // Embedded Redis 현재 실행중인지 확인
  private boolean isRedisRunning() throws IOException {
    return isRunning(executeGrepProcessCommand(redisProperties.getPort()));
  }

  // 현재 PC/서버에서 사용가능한 포트 조회
  public int findAvailablePort() throws IOException {
    for (int port = 10000; port <= 65535; port++) {
      Process process = executeGrepProcessCommand(port);
      if (!isRunning(process))
        return port;
    }

    throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
  }

  // 해당 port 사용중인 프로세스 확인하는 sh 실행
  private Process executeGrepProcessCommand(int port) throws IOException {
    String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
    String[] shell = {"/bin/sh", "-c", command};
    return Runtime.getRuntime().exec(shell);
  }

  // 해당 Process 현재 실행중인지 확인
  private boolean isRunning(Process process) {
    String line;
    StringBuilder pidInfo = new StringBuilder();

    try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      while ((line = input.readLine()) != null)
        pidInfo.append(line);
    } catch (Exception e) {
      log.info("[FAILED] EMBEDDED-REDIS NOT WORKING");
    }

    return !StringUtils.isEmpty(pidInfo.toString());
  }
}
