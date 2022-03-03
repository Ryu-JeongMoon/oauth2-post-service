package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> probableMember = memberRepository.findActiveByEmail(username);
    probableMember.ifPresent(member -> member.changeLatestAuthProvider(AuthProvider.LOCAL));

    return probableMember
        .map(UserPrincipal::from)
        .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.MEMBER_NOT_FOUND));
  }
}
