package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityListeners;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, attributes);

    Member member = getMember(registrationId, oAuth2Attributes);
    return UserPrincipal.from(member, oAuth2Attributes.getAttributes());
  }

  private Member getMember(String registrationId, OAuth2Attributes attributes) {
    Optional<Member> probableMember = memberRepository.findActiveByEmail(attributes.getEmail());
    probableMember.ifPresent(member ->
        member.changeLatestAuthProvider(EnumUtils.getEnumIgnoreCase(AuthProvider.class, registrationId))
    );

    return probableMember.orElseGet(() -> getNewlyRegistered(registrationId, attributes));
  }

  private Member getNewlyRegistered(String registrationId, OAuth2Attributes oAuth2Attributes) {
    Member member = oAuth2Attributes.toMember(registrationId);
    return memberRepository.save(member);
  }
}
