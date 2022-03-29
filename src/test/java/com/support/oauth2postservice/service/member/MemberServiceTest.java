package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.ServiceTest;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
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

  private final Member member = MemberTestHelper.createUser();
  private final MemberSignupRequest userRequest = MemberTestHelper.createUserSignupRequest();
  private final MemberReadResponse memberReadResponse = MemberReadResponse.from(member);
  private final MemberDeleteRequest deleteRequest = MemberTestHelper.createDeleteRequest(USER_ID);

  @BeforeEach
  void setUp() {
    member.changeToEncodedPassword(passwordEncoder.encode(member.getPassword()));
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
    when(memberRepository.findById(nullable(String.class))).thenReturn(Optional.ofNullable(member));

    memberService.edit(MemberTestHelper.createDefaultEditRequest());

    assertThat(member.getNickname()).isEqualTo(MemberTestHelper.USER_NICKNAME_AFTER_EDIT);

    verify(memberRepository, times(1)).findById((String) any());
  }

  @Nested
  @DisplayName("회원 조회")
  class FindMemberTest {

    @Test
    @DisplayName("ID 조회 성공")
    void findActiveMemberById() {
      when(memberRepository.findResponseById(USER_ID)).thenReturn(Optional.of(memberReadResponse));

      MemberReadResponse memberReadResponse = memberService.findResponseById(USER_ID);

      assertThat(memberReadResponse.getEmail()).isEqualTo(member.getEmail());

      verify(memberRepository, times(1)).findResponseById(USER_ID);
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
      when(memberRepository.findResponseById(USER_ID)).thenThrow(new JpaObjectRetrievalFailureException(new EntityNotFoundException()));

      assertThrows(JpaObjectRetrievalFailureException.class, () -> memberService.findResponseById(USER_ID));

      verify(memberRepository, times(1)).findResponseById(any());
    }
  }

  @Nested
  @DisplayName("회원 탈퇴")
  class LeaveTest {

    @Test
    @DisplayName("탈퇴 성공 - 유저 본인")
    void leave_byOwner() {
      when(memberRepository.findActive(USER_ID)).thenReturn(Optional.of(member));

      memberService.leave(Role.USER, deleteRequest);

      assertThat(member.getLeftAt()).isNotNull();

      verify(memberRepository, times(1)).findActive(USER_ID);
    }

    @Test
    @DisplayName("탈퇴 성공 - 관리자")
    void leave_byAdmin() {
      when(memberRepository.findActive(USER_ID)).thenReturn(Optional.of(member));

      memberService.leave(Role.ADMIN, deleteRequest);

      assertThat(member.getLeftAt()).isNotNull();

      verify(memberRepository, times(1)).findActive(USER_ID);
    }

    @Test
    @DisplayName("탈퇴 실패 - 존재하지 않는 ID")
    void leave_failByWrongId() {
      when(memberRepository.findActive(AdditionalMatchers.not(eq(MANAGER_ID)))).thenThrow(IllegalArgumentException.class);

      assertThrows(IllegalArgumentException.class, () -> memberService.leave(Role.USER, deleteRequest));

      verify(memberRepository, times(1)).findActive(any());
    }

    @Test
    @DisplayName("탈퇴 실패 - 이미 탈퇴한 회원 IllegalStateException 발생")
    void leave_failByAlreadyLeft() {
      member.leave();

      when(memberRepository.findActive(any())).thenReturn(Optional.of(member));

      assertThrows(IllegalStateException.class, () -> memberService.leave(Role.USER, deleteRequest));
    }

    @Test
    @DisplayName("탈퇴 실패 - 비밀번호 불일치")
    void leave_failByEmptyAuthentication() {
      when(memberRepository.findActive(any())).thenReturn(Optional.of(member));

      MemberDeleteRequest deleteRequest = MemberDeleteRequest.builder()
          .id("panda")
          .password("WRONG PASSWORD")
          .build();

      assertThrows(
          IllegalArgumentException.class,
          () -> memberService.leave(Role.USER, deleteRequest));
    }
  }
}