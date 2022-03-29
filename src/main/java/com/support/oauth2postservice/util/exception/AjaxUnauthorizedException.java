package com.support.oauth2postservice.util.exception;

public class AjaxUnauthorizedException extends RuntimeException {

  public AjaxUnauthorizedException() {
    super();
  }

  public AjaxUnauthorizedException(String message) {
    super(message);
  }

  public AjaxUnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AjaxUnauthorizedException(Throwable cause) {
    super(cause);
  }

  protected AjaxUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
