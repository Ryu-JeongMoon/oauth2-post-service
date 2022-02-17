package com.support.oauth2postservice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ExceptionResponse {

  private final String exception;
  private final String message;

  public static ExceptionResponse of(String exception, String message) {
    return new ExceptionResponse(exception, message);
  }
}
