package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.TokenFactory;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.service.dto.request.LoginRequest;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.exception.AjaxIllegalArgumentException;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenFactory tokenFactory;
  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public TokenResponse getTokenAfterLogin(LoginRequest loginRequest) {
    Optional<Member> probableMember = memberRepository.findActiveByEmail(loginRequest.getEmail());

    return probableMember
        .map(member -> {
          throwIfPasswordNotMatches(loginRequest, member);

          Authentication authentication = UserPrincipal
              .from(member)
              .toAuthentication();

          SecurityUtils.setAuthentication(authentication);
          return tokenFactory.create(authentication);
        })
        .orElseThrow(() -> new AjaxIllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));
  }

  private void throwIfPasswordNotMatches(LoginRequest loginRequest, Member member) {
    if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
      throw new BadCredentialsException(ExceptionMessages.Member.PASSWORD_NOT_CORRECT);
  }
}
