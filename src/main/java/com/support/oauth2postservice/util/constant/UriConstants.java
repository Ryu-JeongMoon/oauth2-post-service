package com.support.oauth2postservice.util.constant;

public class UriConstants {

  public static class Full {

    public static final String BASE_URL = "https://localhost:8443";
    public static final String TOKEN_CALLBACK_URI = "https://localhost:8443/token/google";

    public static final String VERIFICATION_URI = "https://oauth2.googleapis.com/tokeninfo";
    public static final String TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
  }

  public static class Mapping {

    public static final String REISSUE_TOKEN = "/token/{registrationId}";
    public static final String VALIDATE_TOKEN = "/token/validation";
    public static final String DEFAULT_REDIRECT_URL_PREFIX = "/login/oauth2/code/";
  }
}
