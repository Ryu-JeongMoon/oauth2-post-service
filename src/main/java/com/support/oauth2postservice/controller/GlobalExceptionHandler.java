package com.support.oauth2postservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.oauth2postservice.util.exception.AjaxIllegalArgumentException;
import com.support.oauth2postservice.util.exception.AjaxUnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Profile("!prod & !test")
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper;

  private static String getExceptionClass(Exception e) {
    return e != null ? e.getClass().getName() : "";
  }

  @ExceptionHandler(value = {AjaxUnauthorizedException.class, AjaxIllegalArgumentException.class})
  public ResponseEntity<ExceptionResponse> forbiddenHandler(AjaxUnauthorizedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.of(getExceptionClass(e), e.getMessage()));
  }

  @SneakyThrows(value = JsonProcessingException.class)
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public String notValidMethodArgumentHandler(MethodArgumentNotValidException e, Model model) {
    String message = objectMapper.writeValueAsString(e.getBindingResult());
    model.addAttribute("message", message);
    return "error/4xx";
  }

  @ExceptionHandler(value = RuntimeException.class)
  public String handle4xxError(Exception e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error/4xx";
  }

  @ExceptionHandler(value = Exception.class)
  public String handle5xxError(Exception e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error/5xx";
  }
}
