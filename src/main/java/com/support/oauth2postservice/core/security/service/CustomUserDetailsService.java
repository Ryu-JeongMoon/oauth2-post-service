package com.support.oauth2postservice.core.security.service;

import com.support.oauth2postservice.core.exception.ExceptionMessages;
import com.support.oauth2postservice.core.security.dto.UserPrincipal;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findActiveByEmail(username)
                .map(UserPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.MEMBER_NOT_FOUND));
    }
}
