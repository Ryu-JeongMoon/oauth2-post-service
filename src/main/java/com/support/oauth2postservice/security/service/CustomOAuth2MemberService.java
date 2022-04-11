package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.AuthProvider;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
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
    Optional<Member> probableMember = memberRepository.findByEmail(attributes.getEmail());
    probableMember.ifPresent(member -> {
          throwIfInactive(member);
          member.changeLatestAuthProvider(AuthProvider.caseInsensitiveValueOf(registrationId));
        }
    );

    return probableMember.orElseGet(() -> getNewlyRegistered(registrationId, attributes));
  }

  private void throwIfInactive(Member member) {
    if (Status.INACTIVE.equals(member.getStatus()))
      throw new IllegalStateException(ExceptionMessages.Member.ALREADY_LEFT);
  }

  private Member getNewlyRegistered(String registrationId, OAuth2Attributes oAuth2Attributes) {
    Member member = oAuth2Attributes.toMember(registrationId);
    return memberRepository.save(member);
  }
}
