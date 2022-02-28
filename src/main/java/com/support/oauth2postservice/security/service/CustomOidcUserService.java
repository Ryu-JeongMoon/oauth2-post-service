package com.support.oauth2postservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService implements OAuth2Service {

  private final OAuth2MemberService oAuth2MemberService;

  @Override
  @Transactional
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    return getOAuth2UserPrincipal(userRequest, oAuth2MemberService, oidcUser);
  }
}