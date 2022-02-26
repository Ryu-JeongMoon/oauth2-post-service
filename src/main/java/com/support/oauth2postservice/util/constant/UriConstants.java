package com.support.oauth2postservice.util.constant;

public class UriConstants {


  public static class Full {

    public static final String BASE_URL = "https://localhost:8443";
    public static final String CALLBACK_URL_PREFIX = "https://localhost:8443/oauth2/callback/";
  }

  public static class Mapping {

    public static final String OAUTH2_CALLBACK = "/oauth2/callback/{registrationId}";
    public static final String ACCESS_TOKEN_REISSUE = "to-be-added";
  }
}
