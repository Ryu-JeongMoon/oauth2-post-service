package com.support.oauth2postservice.util;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
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

  public static String getIdFromCurrentUser() {
    Object principal = getPrincipalNullSafely();

    if (principal instanceof UserPrincipal)
      return ((UserPrincipal) principal).getId();
    else if (principal instanceof OAuth2UserPrincipal)
      return ((OAuth2UserPrincipal) principal).getId();

    throw new IllegalStateException(ExceptionMessages.Common.ILLEGAL_STATE);
  }

  public static String getEmailFromCurrentUser() {
    Object principal = getPrincipalNullSafely();

    if (principal instanceof UserPrincipal)
      return ((UserPrincipal) principal).getEmail();
    else if (principal instanceof OAuth2UserPrincipal)
      return ((OAuth2UserPrincipal) principal).getEmail();

    throw new IllegalStateException(ExceptionMessages.Common.ILLEGAL_STATE);
  }

  private static Object getPrincipalNullSafely() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (ObjectUtils.isEmpty(authentication))
      throw new AuthenticationCredentialsNotFoundException(ExceptionMessages.Member.NOT_LOGIN);

    Object principal = authentication.getPrincipal();
    if (ObjectUtils.isEmpty(principal))
      throw new AuthenticationCredentialsNotFoundException(ExceptionMessages.Member.NOT_LOGIN);

    return principal;
  }

  public static void setAuthentication(Authentication authentication) {
    SecurityContextHolder.clearContext();
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
