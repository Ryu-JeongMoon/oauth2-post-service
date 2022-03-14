package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshTokenTestHelper {

  private static final String TOKEN_VALUE = UUID.randomUUID().toString();

  public static RefreshToken create() {
    Member member = MemberTestHelper.createUser();

    return RefreshToken.builder()
        .member(member)
        .tokenValue(TOKEN_VALUE)
        .expiredAt(LocalDateTime.now().plusYears(100))
        .authProvider(member.getInitialAuthProvider())
        .build();
  }
}
