package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Checker {

  private final MemberRepository memberRepository;

  public boolean isOwner(String memberId) {
    String idFromCurrentUser = SecurityUtils.getIdFromCurrentUser();
    return StringUtils.equalsIgnoreCase(idFromCurrentUser, memberId);
  }

  public boolean isAuthorized(String memberId) {
    Role roleFromCurrentUser = SecurityUtils.getRoleFromCurrentUser();
    Member member = memberRepository.findActive(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    return roleFromCurrentUser.isSuperiorThan(member.getRole())
        && roleFromCurrentUser != Role.USER
        || isOwner(memberId);
  }
}
