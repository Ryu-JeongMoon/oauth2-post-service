package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleChecker {

  private final MemberRepository memberRepository;

  public boolean isOwner(String memberId) {
    UserPrincipal currentUser = SecurityUtils.getPrincipalFromCurrentUser();
    return StringUtils.equalsIgnoreCase(currentUser.getId(), memberId);
  }

  public boolean isAuthorized(String memberId) {
    Role roleFromCurrentUser = SecurityUtils.getRoleFromCurrentUser();
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    return roleFromCurrentUser.isSuperiorThan(member.getRole())
        && roleFromCurrentUser != Role.USER
        || isOwner(memberId);
  }
}
