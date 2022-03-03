package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.security.dto.OAuth2UserPrincipal;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {

  default OAuth2UserPrincipal getOAuth2UserPrincipal(OAuth2UserRequest userRequest, OAuth2MemberService service, OAuth2User oAuth2User) {
    OAuth2Token oAuth2Token = userRequest.getAccessToken();
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, oAuth2User.getAttributes());

    Member member = service.getMember(registrationId, oAuth2Attributes);
    return OAuth2UserPrincipal.of(member, oAuth2User, oAuth2Token);
  }
}
