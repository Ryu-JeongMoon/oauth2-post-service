package com.support.oauth2postservice.util.exception;

public class ExceptionMessages {

  public static final String MEMBER_NOT_FOUND = "존재하지 않는 회원입니다";
  public static final String MEMBER_ALREADY_LEFT = "이미 탈퇴한 회원입니다";
  public static final String MEMBER_ACCESS_DENIED = "요청 권한이 없습니다";
  public static final String PASSWORD_NOT_ENCODED = "비밀번호 암호화에 실패했습니다";

  public static final String POST_NOT_FOUND = "존재하지 않는 게시글입니다";
  public static final String POST_INCORRECT_DATE = "시작 시간이 종료 시간보다 늦을 수 없습니다";

  public static class Token {

    public static final String NOT_SIGNED = "토큰에 서명할 수 없습니다";
    public static final String NOT_VALID = "토큰의 데이터가 올바르지 않습니다";
    public static final String WRONG_FORMAT = "토큰의 형태가 올바르지 않습니다";
    public static final String REQUEST_REJECTED = "토큰 요청이 실패했습니다";
    public static final String VALIDATION_REJECTED = "토큰 검증이 실패했습니다";
  }

  public static class Common {

    public static final String UNSUPPORTED_OPERATION = "지원하지 않는 요청입니다";
  }
}
