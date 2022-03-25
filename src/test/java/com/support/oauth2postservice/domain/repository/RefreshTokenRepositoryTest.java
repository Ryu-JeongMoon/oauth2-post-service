package com.support.oauth2postservice.domain.repository;

import com.support.oauth2postservice.config.AbstractDataJpaTest;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RefreshTokenRepositoryTest extends AbstractDataJpaTest {

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  RefreshToken refreshToken;
  Member member;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    memberRepository.save(member);

    refreshToken = RefreshToken.builder()
        .member(member)
        .expiredAt(LocalDateTime.MAX)
        .authProvider(AuthProvider.GOOGLE)
        .tokenValue("YAHOO")
        .build();
    refreshTokenRepository.save(refreshToken);
  }

  @Test
  @DisplayName("이메일로 조회")
  void findByEmail() {
    boolean present = refreshTokenRepository.findByEmail(member.getEmail()).isPresent();

    assertTrue(present);
  }

  @Test
  @DisplayName("회원으로 조회")
  void findByMember() {
    boolean present = refreshTokenRepository.findByMember(member).isPresent();

    assertTrue(present);
  }

  @Test
  @DisplayName("토큰 값으로 조회")
  void findByTokenValue() {
    boolean present = refreshTokenRepository.findByTokenValue(refreshToken.getTokenValue()).isPresent();

    assertTrue(present);
  }
}