package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.exception.AjaxIllegalArgumentException;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public MemberReadResponse findResponseById(String memberId) {
    return memberRepository.findResponseById(memberId)
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
  public void edit(MemberEditRequest memberEditRequest) {
    if (memberEditRequest == null)
      return;

    Member member = memberRepository.findById(memberEditRequest.getId())
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    changePasswordIfValid(member, memberEditRequest.getPassword());
    member.changeBy(memberEditRequest.getNickname(), memberEditRequest.getRole(), memberEditRequest.getStatus());
  }

  private void changePasswordIfValid(Member member, String password) {
    if (StringUtils.isBlank(password))
      return;

    if (password.length() < ColumnConstants.Length.PASSWORD_MIN)
      throw new IllegalArgumentException(ExceptionMessages.Member.PASSWORD_WRONG_FORMAT);

    String encodedPassword = passwordEncoder.encode(password);
    member.changeToEncodedPassword(encodedPassword);
  }

  @Transactional
  public void leave(Role roleFromCurrentUser, MemberDeleteRequest memberDeleteRequest) {
    Optional<Member> probableMember = memberRepository.findActiveById(memberDeleteRequest.getId());

    if (roleFromCurrentUser == Role.ADMIN) {
      probableMember.ifPresent(Member::leave);
      return;
    }

    probableMember.ifPresent(
        member -> {
          if (!passwordEncoder.matches(memberDeleteRequest.getPassword(), member.getPassword()))
            throw new AjaxIllegalArgumentException(ExceptionMessages.Member.NOT_FOUND);
          member.leave();
        });
  }
}