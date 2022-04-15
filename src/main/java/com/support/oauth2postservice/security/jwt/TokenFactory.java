package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.constant.Times;
import org.springframework.security.core.Authentication;

import java.util.Date;

public abstract class TokenFactory {

  public TokenResponse create(Authentication authentication) {
    String idToken = createIdToken(authentication);
    String accessToken = createAccessToken(authentication);
    String refreshToken = createRefreshToken(authentication);

    return TokenResponse.builder()
        .idToken(idToken)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public TokenResponse renew(Authentication authentication, String refreshToken) {
    String idToken = createIdToken(authentication);
    String accessToken = createAccessToken(authentication);

    return TokenResponse.builder()
        .idToken(idToken)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  protected abstract String createIdToken(Authentication authentication);

  protected abstract String createAccessToken(Authentication authentication);

  protected abstract String createRefreshToken(Authentication authentication);

  /**
   * 만료 시간은 현재 시간부터 만료 시간을 더함으로써 정해진다<br/>
   * 토큰 생성을 위한 nimbus jwt 에서는 Date 로 계산하기 때문에<br/>
   * millis 단위의 계산을 수행 후 이를 인자로 새로운 Date 인스턴스를 만들어 반환한다<br/>
   */
  protected Date createExpirationDate(Times times) {
    long currentTimeInMillis = new Date().getTime();
    long expirationTimeInMillis = Times.toMillis(times);
    return new Date(currentTimeInMillis + expirationTimeInMillis);
  }
}
