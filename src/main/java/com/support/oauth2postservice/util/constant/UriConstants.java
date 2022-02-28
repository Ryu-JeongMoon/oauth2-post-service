package com.support.oauth2postservice.util.constant;

public class UriConstants {

  public static class Full {

    public static final String BASE_URL = "https://localhost:8443";
    public static final String CALLBACK_URL_PREFIX = "https://localhost:8443/login/oauth2/code/";
    public static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";
  }

  public static class Mapping {

    public static final String DEFAULT_REDIRECT_URL_PREFIX = "/login/oauth2/code/";
    public static final String OAUTH2_CALLBACK = "/login/oauth2/code/{registrationId}";
    public static final String ACCESS_TOKEN_REISSUE = "to-be-added";
  }
}
