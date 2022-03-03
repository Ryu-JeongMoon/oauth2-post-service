package com.support.oauth2postservice.util.constant;

public class UriConstants {

  public static class Full {
    public static final String BASE_URL = "https://localhost:8443";
    public static final String MY_PAGE = "https://localhost:8443/members/my-page";

    public static final String TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    public static final String TOKEN_CALLBACK_URI = "https://localhost:8443/token/google";
    public static final String VERIFICATION_URI = "https://oauth2.googleapis.com/tokeninfo";
  }

  public static class Mapping {
    public static final String MEMBERS = "/members";
    public static final String MY_PAGE = "/members/my-page";

    public static final String ISSUE_TOKEN = "/token/{registrationId}";
    public static final String RENEW_TOKEN = "/token/{registrationId}/renewal";
    public static final String VALIDATE_TOKEN = "/token/validation";
    public static final String DEFAULT_REDIRECT_URL_PREFIX = "/login/oauth2/code/";
  }
}
