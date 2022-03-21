package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.RefreshTokenRepository;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

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
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail())
        .map(token -> token.withTokenValue(tokenValue))
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
}
