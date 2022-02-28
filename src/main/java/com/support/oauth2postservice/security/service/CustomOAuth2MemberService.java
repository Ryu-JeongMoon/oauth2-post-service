package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityListeners;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class CustomOAuth2MemberService implements OAuth2MemberService {

  private final MemberRepository memberRepository;

  public Member getMember(String registrationId, OAuth2Attributes attributes) {
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
