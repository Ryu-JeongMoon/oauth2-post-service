package com.support.oauth2postservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Profile("prod | test")
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestExceptionHandler {

  private final ObjectMapper objectMapper;

  private static String getExceptionClass(Exception e) {
    return e != null ? e.getClass().getName() : "";
  }

  @ExceptionHandler(value = {
      IllegalArgumentException.class, PersistenceException.class,
      JsonProcessingException.class, ConstraintViolationException.class, ArithmeticException.class
  })
  public ResponseEntity<ExceptionResponse> badRequestHandler(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @ExceptionHandler(value = AuthenticationException.class)
  public ResponseEntity<ExceptionResponse> unauthorizedHandler(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<ExceptionResponse> forbiddenHandler(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @ExceptionHandler(value = MethodNotAllowedException.class)
  public ResponseEntity<ExceptionResponse> methodNowAllowedHandler(MethodNotAllowedException e) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @ExceptionHandler(value = {
      IllegalStateException.class, NullPointerException.class
  })
  public ResponseEntity<ExceptionResponse> internalServerErrorHandler(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ExceptionResponse> anyOtherExceptionHandler(Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @SneakyThrows(value = JsonProcessingException.class)
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<String> notValidMethodArgumentHandler(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.writeValueAsString(e.getBindingResult()));
  }
}
