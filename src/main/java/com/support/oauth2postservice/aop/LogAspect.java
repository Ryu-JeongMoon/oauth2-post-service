package com.support.oauth2postservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class LogAspect {

  @AfterReturning(value = "bean(*ExceptionHandler)")
  public void logForException(JoinPoint joinPoint) {
    log.info("[SUPPORT-ERROR] :: method     -> {}", joinPoint.getSignature().toShortString());
    log.info("[SUPPORT-ERROR] :: arguments  -> {}", joinPoint.getArgs());

    CompletableFuture.runAsync(
        () -> Arrays.stream(joinPoint.getArgs())
            .filter(arg -> arg instanceof Exception)
            .forEach(arg -> log.error("[SUPPORT-ERROR] :: exception {}", (Exception) arg))
    );

    Arrays.stream(joinPoint.getArgs())
        .filter(arg -> arg instanceof Exception)
        .flatMap(arg -> Arrays.stream(((Exception) arg).getStackTrace()))
        .limit(1)
        .forEach(stackTraceElement -> log.info("[SUPPORT-ERROR] :: exception  -> {}", stackTraceElement));
  }
}
