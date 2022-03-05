package com.support.oauth2postservice.util.constant;

public class SpELConstants {
  public static final String ANONYMOUS_ONLY = "isAnonymous()";
  public static final String ANY_ROLE_ALLOWED = "hasRole('USER')";
  public static final String MANAGER_GOE_ALLOWED = "hasRole('MANGER')";
  public static final String ADMIN_ONLY_ALLOWED = "hasRole('ADMIN')";

  public static final String EXCEPTION_HANDLER_ONLY = "bean(*ExceptionHandler)";
}
