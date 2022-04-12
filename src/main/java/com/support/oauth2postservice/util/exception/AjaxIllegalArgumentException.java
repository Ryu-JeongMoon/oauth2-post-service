package com.support.oauth2postservice.util.exception;

public class AjaxIllegalArgumentException extends RuntimeException {

  public AjaxIllegalArgumentException() {
    super();
  }

  public AjaxIllegalArgumentException(String message) {
    super(message);
  }

  public AjaxIllegalArgumentException(String message, Throwable cause) {
    super(message, cause);
  }

  public AjaxIllegalArgumentException(Throwable cause) {
    super(cause);
  }

  protected AjaxIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
