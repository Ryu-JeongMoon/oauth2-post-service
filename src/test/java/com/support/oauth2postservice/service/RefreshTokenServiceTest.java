package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PrincipalTestHelper;
import com.support.oauth2postservice.helper.RefreshTokenTestHelper;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("리프레시 토큰 서비스 테스트")
class RefreshTokenServiceTest extends ServiceTest {

  private final Member member = MemberTestHelper.createUser();
  private final RefreshToken refreshToken = RefreshTokenTestHelper.create();
  private final OAuth2UserPrincipal oAuth2UserPrincipal = PrincipalTestHelper.createOAuth2UserPrincipal();

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("이메일로 조회")
  void findByEmail() {
    when(refreshTokenRepository.findByEmail(any())).thenReturn(Optional.of(refreshToken));

    RefreshTokenResponse tokenResponse = refreshTokenService.findByEmail(anyString());

    assertAll(
        () -> assertThat(tokenResponse.getTokenValue()).isEqualTo(refreshToken.getTokenValue()),
        () -> assertThat(tokenResponse.getAuthProvider()).isEqualTo(refreshToken.getAuthProvider())
    );

    verify(refreshTokenRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @DisplayName("회원으로 조회")
  void findByMember() {
    when(refreshTokenRepository.findByMember(any(Member.class))).thenReturn(Optional.of(refreshToken));

    RefreshTokenResponse tokenResponse = refreshTokenService.findByMember(member);

    assertAll(
        () -> assertThat(tokenResponse.getTokenValue()).isEqualTo(refreshToken.getTokenValue()),
        () -> assertThat(tokenResponse.getAuthProvider()).isEqualTo(refreshToken.getAuthProvider())
    );

    verify(refreshTokenRepository, times(1)).findByMember(any(Member.class));
  }

  @Test
  @DisplayName("토큰 값으로 조회")
  void findByTokenValue() {
    when(refreshTokenRepository.findByTokenValue(anyString())).thenReturn(Optional.of(refreshToken));

    RefreshTokenResponse tokenResponse = refreshTokenService.findByTokenValue("YAHOO");

    assertAll(
        () -> assertThat(tokenResponse.getTokenValue()).isEqualTo(refreshToken.getTokenValue()),
        () -> assertThat(tokenResponse.getAuthProvider()).isEqualTo(refreshToken.getAuthProvider())
    );

    verify(refreshTokenRepository, times(1)).findByTokenValue(anyString());
  }

  @Test
  @DisplayName("기존 회원으로 토큰 추가")
  void saveOrUpdate() {
    when(memberRepository.findActiveByEmail(any())).thenReturn(Optional.of(member));
    when(refreshTokenRepository.findByEmail(any())).thenReturn(Optional.of(refreshToken));

    refreshTokenService.saveOrUpdate(oAuth2UserPrincipal, "YAHOO");

    verify(memberRepository, times(1)).findActiveByEmail(any());
    verify(refreshTokenRepository, times(1)).findByEmail(any());
    verify(refreshTokenRepository, times(1)).save(refreshToken);
  }
}