package com.support.oauth2postservice.util.constant;

public class SpELConstants {
  public static final String ANONYMOUS_ONLY = "isAnonymous()";
  public static final String USER_ONLY = "hasRole('USER')";
  public static final String MANAGER_ONLY = "hasRole('MANAGER')";
  public static final String ADMIN_ONLY = "hasRole('ADMIN')";

  public static final String ANY_ROLE_ALLOWED = "hasAnyRole('USER', 'MANAGER', 'ADMIN')";
  public static final String MANAGER_OR_ADMIN = "hasAnyRole('MANAGER', 'ADMIN')";
  public static final String ANONYMOUS_OR_ADMIN = "isAnonymous() or hasRole('ADMIN')";

  public static final String CONTROLLER_ONLY = "bean(*Controller)";
  public static final String EXCEPTION_HANDLER_ONLY = "bean(*ExceptionHandler)";
}
