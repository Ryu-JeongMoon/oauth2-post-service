package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.RefreshTokenRepository;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private static final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional(readOnly = true)
  public RefreshTokenResponse findByEmail(String email) {
    return refreshTokenRepository.findByEmail(email)
        .map(RefreshTokenResponse::from)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RefreshTokenResponse findByMember(Member member) {
    return refreshTokenRepository.findByMember(member)
        .map(RefreshTokenResponse::from)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RefreshTokenResponse findByTokenValue(String tokenValue) {
    return refreshTokenRepository.findByTokenValue(tokenValue)
        .map(RefreshTokenResponse::from)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional
  public void saveOrUpdate(OAuth2UserPrincipal principal, String tokenValue) {
    Member member = memberRepository.findActiveByEmail(principal.getEmail())
        .orElseGet(() -> getNewlyRegisteredByPrincipal(principal));

    RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail())
        .orElseGet(
            () -> RefreshToken.builder()
                .member(member)
                .tokenValue(tokenValue)
                .expiredAt(LocalDateTime.now().plusYears(1))
                .authProvider(member.getInitialAuthProvider())
                .build()
        );

    refreshTokenRepository.save(refreshToken);
  }

  private Member getNewlyRegisteredByPrincipal(OAuth2UserPrincipal principal) {
    String defaultRandomPassword = RandomStringUtils.randomAlphanumeric(10);
    String encodedRandomPassword = passwordEncoder.encode(defaultRandomPassword);

    Member member = Member.builder()
        .email(principal.getEmail())
        .nickname(principal.getNickName())
        .password(encodedRandomPassword)
        .initialAuthProvider(AuthProvider.GOOGLE)
        .build();

    return memberRepository.save(member);
  }
}
