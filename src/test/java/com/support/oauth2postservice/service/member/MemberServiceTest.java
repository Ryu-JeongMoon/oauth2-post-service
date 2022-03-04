package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.service.ServiceTest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceTest extends ServiceTest {

  private Member user;
  private MemberSignupRequest userRequest;

  @BeforeEach
  void setUp() {
    user = MemberTestHelper.createUser();
    user.changeToEncodedPassword(passwordEncoder.encode(user.getPassword()));
    userRequest = MemberTestHelper.createUserRequest();
    memberService = new MemberService(memberRepository, passwordEncoder);
  }

  @Test
  @DisplayName("회원 가입")
  void join() {
    when(memberRepository.save(any())).thenReturn(any(Member.class));

    memberService.join(userRequest);

    verify(memberRepository, times(1)).save(any(Member.class));
  }

  @Test
  @DisplayName("회원 정보 수정")
  void edit() {
    when(memberRepository.findActive(any())).thenReturn(Optional.ofNullable(user));

    memberService.edit(USER_ID, MemberTestHelper.createEditRequest());

    assertThat(user.getNickname()).isEqualTo(MemberTestHelper.USER_NICKNAME_AFTER_EDIT);

    verify(memberRepository, times(1)).findActive(USER_ID);
  }

  @Test
  @DisplayName("회원 조회 - 활성 상태")
  void findActiveMember() {
    when(memberRepository.findActive(USER_ID)).thenReturn(Optional.of(user));

    MemberReadResponse memberReadResponse = memberService.findActiveMemberById(USER_ID);

    assertThat(memberReadResponse.getEmail()).isEqualTo(user.getEmail());

    verify(memberRepository, times(1)).findActive(USER_ID);
  }

  @Test
  @DisplayName("회원 조회 실패 - 존재하지 않는 회원")
  void failFindActiveMember() {
    when(memberRepository.findActive(USER_ID)).thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

    assertThrows(JpaObjectRetrievalFailureException.class, () -> memberService.findActiveMemberById(USER_ID));

    verify(memberRepository, times(1)).findActive(USER_ID);
  }

  @Test
  @DisplayName("회원 탈퇴")
  void leave() {
    when(memberRepository.findActive(USER_ID)).thenReturn(Optional.of(user));

    memberService.leave(USER_ID);

    assertThat(user.getLeftAt()).isNotNull();

    verify(memberRepository, times(1)).findActive(USER_ID);
  }
}