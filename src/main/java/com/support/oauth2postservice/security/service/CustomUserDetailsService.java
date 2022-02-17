package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    memberRepository.findActiveByEmail(username)
        .ifPresent(member -> member.synchronizeLatestAuthProvider(AuthProvider.LOCAL));

    return memberRepository.findActiveByEmail(username)
        .map(UserPrincipal::from)
        .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.MEMBER_NOT_FOUND));
  }
}
