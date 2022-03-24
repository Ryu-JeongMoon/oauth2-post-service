package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class CustomOAuth2MemberServiceTest {

  @Mock
  MemberRepository memberRepository;
  @InjectMocks
  CustomOAuth2MemberService customOAuth2MemberService;

  private final Member member = MemberTestHelper.createUser();
  private final Member inactiveMember = MemberTestHelper.createUser();

  @BeforeEach
  void setUp() {
    inactiveMember.changeBy(null, null, Status.INACTIVE);
  }

  @Test
  @DisplayName("회원 조회 성공")
  void getMember() {
    when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", member.getEmail());
    attributes.put("name", member.getNickname());
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of("google", attributes);
    Member result = customOAuth2MemberService.getMember("google", oAuth2Attributes);

    Assertions.assertThat(result.getEmail()).isEqualTo(member.getEmail());

    verify(memberRepository, times(1)).findByEmail(any());
  }

  @Test
  @DisplayName("회원 조회 실패 - 비활성 상태")
  void getMember_failByStatus() {
    when(memberRepository.findByEmail(inactiveMember.getEmail())).thenReturn(Optional.of(inactiveMember));

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", inactiveMember.getEmail());
    attributes.put("name", inactiveMember.getNickname());
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of("google", attributes);

    assertThrows(IllegalStateException.class,
        () -> customOAuth2MemberService.getMember("google", oAuth2Attributes));
  }
}