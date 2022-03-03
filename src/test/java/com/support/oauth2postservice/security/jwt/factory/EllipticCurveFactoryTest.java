package com.support.oauth2postservice.security.jwt.factory;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.EllipticCurveFactory;
import com.support.oauth2postservice.security.jwt.EllipticCurveVerifier;
import com.support.oauth2postservice.security.jwt.TokenException;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EC 알고리즘 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {EllipticCurveFactory.class, EllipticCurveVerifier.class})
class EllipticCurveFactoryTest {

  @Autowired
  EllipticCurveFactory ellipticCurveFactory;
  @Autowired
  EllipticCurveVerifier ellipticCurveVerifier;

  private TokenResponse tokenResponse;

  @BeforeAll
  void setUp() {
    Member member = MemberTestHelper.createUser();
    UserPrincipal principal = UserPrincipal.from(member);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        principal, "",
        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString())));

    tokenResponse = ellipticCurveFactory.create(authenticationToken);
  }

  @Test
  @DisplayName("키 생성 확인")
  void checkKeyGeneration() {
    assertThat(tokenResponse).isNotNull();
  }

  @Nested
  @DisplayName("키 검증 확인")
  class EllipticCurveVerifierTest {

    @Test
    @DisplayName("Access Token 확인 성공")
    void isValid_accessToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getAccessToken());
      assertTrue(validity);
    }

    @Test
    @DisplayName("Access Token 확인 실패")
    void isValid_accessToken_failByWrongToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getAccessToken() + 1);
      assertFalse(validity);
    }

    @Test
    @DisplayName("Refresh Token 확인")
    void isValid_refreshToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getRefreshToken());
      assertTrue(validity);
    }

    @Test
    @DisplayName("Refresh Token 확인 실패")
    void isValid_refreshToken_failByWrongToken() {
      boolean validity = ellipticCurveVerifier.isValid(tokenResponse.getRefreshToken() + 1);
      assertFalse(validity);
    }

    @Test
    @DisplayName("Token 확인 실패 - 빈 토큰 값")
    void isValid_token_failByEmptyToken() {
      assertThrows(TokenException.class, () -> ellipticCurveVerifier.isValid(""));
    }
  }
}