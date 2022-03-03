package com.support.oauth2postservice.util;

import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (ObjectUtils.isEmpty(authentication))
      throw new AuthenticationCredentialsNotFoundException(ExceptionMessages.Member.NOT_LOGIN);

    return getEmailFromPrincipal(authentication.getPrincipal());
  }

  private static String getEmailFromPrincipal(Object principal) {
    if (principal instanceof UserDetails)
      return ((UserDetails) principal).getUsername();

    if (principal instanceof OAuth2AuthenticatedPrincipal)
      return ((OAuth2UserPrincipal) principal).getEmail();

    throw new IllegalStateException(ExceptionMessages.Common.ILLEGAL_STATE);
  }
}
