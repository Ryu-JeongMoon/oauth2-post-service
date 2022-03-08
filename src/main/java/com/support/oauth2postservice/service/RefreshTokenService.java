package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.RefreshTokenRepository;
import com.support.oauth2postservice.service.dto.response.RefreshTokenResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

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
}
