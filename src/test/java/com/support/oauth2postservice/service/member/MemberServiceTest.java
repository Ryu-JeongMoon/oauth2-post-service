package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.service.ServiceTest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceTest extends ServiceTest {

  private Member member;
  private MemberSignupRequest userRequest;
  private MemberReadResponse memberReadResponse;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    member.changeToEncodedPassword(passwordEncoder.encode(member.getPassword()));
    userRequest = MemberTestHelper.createUserRequest();
    memberReadResponse = MemberReadResponse.from(member);
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
    when(memberRepository.findActive(any())).thenReturn(Optional.ofNullable(member));

    memberService.edit(MemberTestHelper.createEditRequest());

    assertThat(member.getNickname()).isEqualTo(MemberTestHelper.USER_NICKNAME_AFTER_EDIT);

    verify(memberRepository, times(1)).findActive(any());
  }

  @Nested
  @DisplayName("회원 조회")
  class FindMemberTest {

    @Test
    @DisplayName("ID 조회 성공")
    void findActiveMemberById() {
      when(memberRepository.findActiveToResponse(USER_ID)).thenReturn(Optional.of(memberReadResponse));

      MemberReadResponse memberReadResponse = memberService.findActiveMemberById(USER_ID);

      assertThat(memberReadResponse.getEmail()).isEqualTo(member.getEmail());

      verify(memberRepository, times(1)).findActiveToResponse(USER_ID);
    }

    @Test
    @DisplayName("Email 조회 성공")
    void findActiveMemberByEmail() {
      when(memberRepository.findActiveByEmail(isNotNull())).thenReturn(Optional.of(member));

      MemberReadResponse memberReadResponse = memberService.findActiveMemberByEmail(anyString());

      assertThat(memberReadResponse.getEmail()).isEqualTo(member.getEmail());

      verify(memberRepository, times(1)).findActiveByEmail(any());
    }

    @Test
    @DisplayName("조회 실패 - 존재하지 않는 회원")
    void findActiveMember_failByNonExistsMember() {
      when(memberRepository.findActiveToResponse(USER_ID)).thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

      assertThrows(JpaObjectRetrievalFailureException.class, () -> memberService.findActiveMemberById(USER_ID));

      verify(memberRepository, times(1)).findActiveToResponse(any());
    }
  }

  @Nested
  @DisplayName("회원 탈퇴")
  class LeaveTest {

    @Test
    @DisplayName("탈퇴 성공")
    void leave() {
      when(memberRepository.findActive(USER_ID)).thenReturn(Optional.of(member));

      memberService.leave(USER_ID);

      assertThat(member.getLeftAt()).isNotNull();

      verify(memberRepository, times(1)).findActive(USER_ID);
    }

    @Test
    @DisplayName("탈퇴 실패 - 존재하지 않는 ID")
    void leave_failByWrongId() {
      when(memberRepository.findActive(AdditionalMatchers.not(eq(USER_ID)))).thenThrow(IllegalArgumentException.class);

      assertThrows(IllegalArgumentException.class, () -> memberService.leave(MANAGER_ID));

      verify(memberRepository, times(1)).findActive(any());
    }

    @Test
    @DisplayName("탈퇴 실패 - 이미 탈퇴한 회원 IllegalStateException 발생")
    void leave_failByAlreadyLeft() {
      member.leave();

      when(memberRepository.findActive(any())).thenReturn(Optional.of(member));

      assertThrows(IllegalStateException.class, () -> memberService.leave(any()));
    }
  }
}