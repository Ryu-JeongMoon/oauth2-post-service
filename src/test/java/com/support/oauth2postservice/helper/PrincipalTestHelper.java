package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.dto.UserPrincipal;

import java.util.Collections;

public class PrincipalTestHelper {

  public static final String ID = "panda";
  public static final String EMAIL = "bear";

  public static UserPrincipal createUserPrincipal() {
    return UserPrincipal.of(ID, EMAIL, Collections.singletonList(Role.USER));
  }

  public static UserPrincipal createManagerPrincipal() {
    return UserPrincipal.of(ID, EMAIL, Collections.singletonList(Role.MANAGER));
  }

  public static UserPrincipal createAdminPrincipal() {
    return UserPrincipal.of(ID, EMAIL, Collections.singletonList(Role.ADMIN));
  }

  public static OAuth2UserPrincipal createOAuth2UserPrincipal() {
    return OAuth2UserPrincipal.from(MemberTestHelper.createUser());
  }

  public static OAuth2UserPrincipal createOAuth2ManagerPrincipal() {
    return OAuth2UserPrincipal.from(MemberTestHelper.createManger());
  }

  public static OAuth2UserPrincipal createOAuth2AdminPrincipal() {
    return OAuth2UserPrincipal.from(MemberTestHelper.createAdmin());
  }
}
