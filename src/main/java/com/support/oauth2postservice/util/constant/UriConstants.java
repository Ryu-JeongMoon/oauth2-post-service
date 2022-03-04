package com.support.oauth2postservice.util.constant;

public class UriConstants {

  public static class Full {
    public static final String BASE_URL = "https://localhost:8443";
    public static final String MY_PAGE = "https://localhost:8443/members/my-page";

    public static final String TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    public static final String TOKEN_CALLBACK_URI = "https://localhost:8443/oauth2/google";
    public static final String VERIFICATION_URI = "https://oauth2.googleapis.com/tokeninfo";
  }

  public static class Mapping {
    public static final String MEMBERS = "/members";
    public static final String MY_PAGE = "/members/my-page";

    public static final String ISSUE_GOOGLE_TOKEN = "/oauth2/google";
    public static final String ISSUE_OAUTH2_TOKEN = "/oauth2/{registrationId}";
    public static final String RENEW_GOOGLE_TOKEN = "/oauth2/google/renewal";
    public static final String RENEW_OAUTH2_TOKEN = "/oauth2/{registrationId}/renewal";
    public static final String RENEW_OAUTH2_TOKEN_AND_REDIRECT = "/oauth2/{registrationId}/renewal/redirect";
    public static final String VALIDATE_OAUTH2_TOKEN = "/oauth2/validation";
    public static final String DEFAULT_REDIRECT_URL_PREFIX = "/login/oauth2/code/";
  }
}
