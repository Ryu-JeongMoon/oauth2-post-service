package com.support.oauth2postservice.util.security.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.service.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest extends ServiceTest {

  private Member member;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
  }

  @Test
  @DisplayName("Security - loadUserByUsername 호출")
  void loadUserByUsername() {
    when(memberRepository.findActiveByEmail(any())).thenReturn(Optional.of(member));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(MemberTestHelper.USER_EMAIL);

    assertThat(userDetails.getUsername()).isEqualTo(MemberTestHelper.USER_EMAIL);
    verify(memberRepository, times(1)).findActiveByEmail(MemberTestHelper.USER_EMAIL);
  }

  @Test
  @DisplayName("Security - loadUserByUsername 호출 시 예외 발생")
  void failLoadUserByUsername() {
    when(memberRepository.findActiveByEmail(any())).thenThrow(RuntimeException.class);

    assertThrows(RuntimeException.class,
        () -> customUserDetailsService.loadUserByUsername(MemberTestHelper.ADMIN_EMAIL));

    verify(memberRepository, times(1)).findActiveByEmail(any());
  }
}