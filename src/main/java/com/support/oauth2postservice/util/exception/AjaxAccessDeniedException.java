package com.support.oauth2postservice.util.exception;

public class AjaxAccessDeniedException extends RuntimeException {

  public AjaxAccessDeniedException() {
    super();
  }

  public AjaxAccessDeniedException(String message) {
    super(message);
  }

  public AjaxAccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AjaxAccessDeniedException(Throwable cause) {
    super(cause);
  }

  protected AjaxAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
