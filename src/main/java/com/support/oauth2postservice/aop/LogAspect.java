package com.support.oauth2postservice.aop;

import com.support.oauth2postservice.util.constant.SpELConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class LogAspect {

  @After(SpELConstants.EXCEPTION_HANDLER_ONLY)
  public void logForException(JoinPoint joinPoint) {
    log.info("[SUPPORT-ERROR] :: method     -> {}", joinPoint.getSignature().toShortString());
    log.info("[SUPPORT-ERROR] :: arguments  -> {}", joinPoint.getArgs());

    CompletableFuture.runAsync(
        () -> Arrays.stream(joinPoint.getArgs())
            .filter(arg -> arg instanceof Exception)
            .forEach(arg -> log.error("[SUPPORT-ERROR] :: exception", (Exception) arg))
    );
  }

  @Around(SpELConstants.CONTROLLER_ONLY)
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start(String.valueOf(joinPoint.getSignature()));

    Object proceed = joinPoint.proceed();

    stopWatch.stop();
    log.info("[SUPPORT-STOPWATCH] :: -> {}", stopWatch.prettyPrint());
    return proceed;
  }
}
