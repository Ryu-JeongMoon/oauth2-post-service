package com.support.oauth2postservice.learning;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.TokenFactory;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.service.dto.request.LoginRequest;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.Optional;

@EnabledOnOs({OS.MAC})
@Transactional
@SpringBootTest
public class SpeedTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  TokenFactory tokenFactory;

  private StopWatch stopWatch;

  private static final LoginRequest loginRequest = LoginRequest.builder()
      .email("panda453@gmail.com")
      .password("1234")
      .build();

  @BeforeEach
  void setUp() {
    stopWatch = new StopWatch();

    Member member = Member.builder()
        .email(loginRequest.getEmail())
        .password(passwordEncoder.encode(loginRequest.getPassword()))
        .role(Role.USER)
        .initialAuthProvider(AuthProvider.LOCAL)
        .nickname("panda")
        .build();
    memberRepository.save(member);
  }

  @RepeatedTest(5)
  @DisplayName("")
  void one() {
    stopWatch.start();
    Member member = memberRepository.findActiveByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
      throw new BadCredentialsException(ExceptionMessages.Member.PASSWORD_NOT_CORRECT);

    Authentication authentication = UserPrincipal
        .from(member)
        .toAuthentication();

    SecurityUtils.setAuthentication(authentication);
    TokenResponse tokenResponse = tokenFactory.create(authentication);
    stopWatch.stop();
    System.out.println("stopWatch.prettyPrint() = " + stopWatch.prettyPrint());
  }

  @RepeatedTest(5)
  @DisplayName("")
  void two() {
    stopWatch.start();
    memberRepository.findActiveByEmail(loginRequest.getEmail())
        .map(member -> {
          if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
            throw new BadCredentialsException(ExceptionMessages.Member.PASSWORD_NOT_CORRECT);

          Authentication authentication = UserPrincipal
              .from(member)
              .toAuthentication();

          SecurityUtils.setAuthentication(authentication);
          TokenResponse tokenResponse = tokenFactory.create(authentication);

          return member;
        })
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));
    stopWatch.stop();
    System.out.println("stopWatch.prettyPrint() = " + stopWatch.prettyPrint());
  }

  @RepeatedTest(5)
  @DisplayName("")
  void three() {
    stopWatch.start();

    Optional<Member> probableMember = memberRepository.findActiveByEmail(loginRequest.getEmail());
    probableMember
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    probableMember
        .ifPresent(
            member -> {
              if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
                throw new BadCredentialsException(ExceptionMessages.Member.PASSWORD_NOT_CORRECT);

              Authentication authentication = UserPrincipal
                  .from(member)
                  .toAuthentication();

              SecurityUtils.setAuthentication(authentication);
              TokenResponse tokenResponse = tokenFactory.create(authentication);
            });

    stopWatch.stop();
    System.out.println("stopWatch.prettyPrint() = " + stopWatch.prettyPrint());
  }
}
