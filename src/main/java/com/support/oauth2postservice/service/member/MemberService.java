package com.support.oauth2postservice.service.member;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.service.member.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public MemberReadResponse findActiveMemberById(String memberId) {
    return memberRepository.findActiveToResponse(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public MemberReadResponse findActiveMemberByEmail(String email) {
    return memberRepository.findActiveByEmail(email)
        .map(MemberReadResponse::from)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public Page<MemberReadResponse> searchByCondition(MemberSearchRequest condition) {
    return memberRepository.search(condition);
  }

  @Transactional
  public void join(MemberSignupRequest memberSignupRequest) {
    Member member = memberSignupRequest.toEntity();
    String encodedPassword = passwordEncoder.encode(memberSignupRequest.getPassword());
    member.changeToEncodedPassword(encodedPassword);
    memberRepository.save(member);
  }

  @Transactional
  public void edit(String memberId, MemberEditRequest memberEditRequest) {
    if (memberEditRequest == null)
      return;

    Member member = memberRepository.findActive(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    String encodedPassword = passwordEncoder.encode(member.getPassword());
    member.changeToEncodedPassword(encodedPassword);
    member.changeBy(memberEditRequest.getNickname(), memberEditRequest.getRole(), memberEditRequest.getStatus());
  }

  @Transactional
  public void leave(String memberId) {
    memberRepository.findActive(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND))
        .leave();
  }
}
