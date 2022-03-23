package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    Authentication auth = UserPrincipal.of(
        UUID.randomUUID().toString(),
        customUser.username(),
        Collections.singletonList(customUser.role())
    ).toAuthentication();

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    return context;
  }
}