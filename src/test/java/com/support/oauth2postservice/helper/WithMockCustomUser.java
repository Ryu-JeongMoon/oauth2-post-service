package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String username() default "username";

  String id() default "5766fe00-f4c5-443e-a41e-f0c42869fef7";

  Role role() default Role.USER;
}
