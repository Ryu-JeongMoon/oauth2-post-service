package com.support.oauth2postservice.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnConstants {

  public static class Length {

    public static final int DEFAULT_MAX = 255;
    public static final int DEFAULT_STRING = 20;

    public static final int ID = 36;
    public static final int EMAIL = 320;
    public static final int NICKNAME = 20;
    public static final int PASSWORD_MIN = 4;
  }

  public static class Name {

    public static final String CREATED_BY = "created_at";
    public static final String CREATED_AT = "created_by";
    public static final String MODIFIED_AT = "modified_at";
    public static final String MODIFIED_BY = "modified_by";

    public static final String MEMBER_ID = "member_id";
    public static final String EMAIL = "email";
    public static final String LEFT_AT = "left_at";
    public static final String NICKNAME = "nickname";
    public static final String UNIQUE_EMAIL = "uk_member_email";
    public static final String UNIQUE_NICKNAME = "uk_member_nickname";

    public static final String POST_ID = "post_id";
    public static final String TITLE = "title";
    public static final String INDEX_TITLE = "ix_title";
    public static final String UNIQUE_TITLE = "uk_post_title";
    public static final String OPENED_AT = "opened_at";
    public static final String CLOSED_AT = "closed_at";
  }
}