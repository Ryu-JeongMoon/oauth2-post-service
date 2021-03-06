package com.support.oauth2postservice.util;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  public static UserPrincipal getPrincipalFromCurrentUser() {
    return getNullSafePrincipal();
  }

  public static Role getRoleFromCurrentUser() {
    return getNullSafePrincipal().getAuthorities()
        .stream()
        .findAny()
        .orElseThrow(() -> new IllegalStateException(ExceptionMessages.Common.ILLEGAL_STATE));
  }

  private static UserPrincipal getNullSafePrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (ObjectUtils.isEmpty(authentication))
      throw new AuthenticationCredentialsNotFoundException(ExceptionMessages.Member.NOT_LOGIN);

    Object principal = authentication.getPrincipal();
    if (ObjectUtils.isEmpty(principal))
      throw new AuthenticationCredentialsNotFoundException(ExceptionMessages.Member.NOT_LOGIN);

    return (UserPrincipal) principal;
  }

  public static void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}