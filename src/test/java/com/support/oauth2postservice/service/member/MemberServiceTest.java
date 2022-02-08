package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.service.ServiceTest;
import com.support.oauth2postservice.service.member.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberServiceTest extends ServiceTest {

    private Member user;
    private MemberSignupRequest userRequest;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, passwordEncoder);

        user = MemberTestHelper.createUser();
        userRequest = MemberTestHelper.createUserRequest();
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
        when(memberRepository.findActiveMember(any())).thenReturn(Optional.ofNullable(user));

        memberService.edit(USER_ID, MemberTestHelper.createEditRequest());

        verify(memberRepository, times(1)).findActiveMember(USER_ID);
    }

    @Test
    @DisplayName("회원 조회 - 활성 상태")
    void findActiveMember() {
        when(memberRepository.findActiveMember(USER_ID)).thenReturn(Optional.of(user));

        MemberReadResponse memberReadResponse = memberService.findActiveMember(USER_ID);

        assertThat(memberReadResponse.getEmail()).isEqualTo(user.getEmail());

        verify(memberRepository, times(1)).findActiveMember(USER_ID);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void leave() {
        when(memberRepository.findActiveMember(USER_ID)).thenReturn(Optional.of(user));

        memberService.leave(USER_ID);

        assertThat(user.getLeftAt()).isNotNull();

        verify(memberRepository, times(1)).findActiveMember(USER_ID);
    }
}