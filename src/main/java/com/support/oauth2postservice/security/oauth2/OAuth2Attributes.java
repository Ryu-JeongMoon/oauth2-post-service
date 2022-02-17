package com.support.oauth2postservice.security.oauth2;

import com.support.oauth2postservice.domain.enumeration.LoginType;
import com.support.oauth2postservice.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
public class OAuth2Attributes {

  private static final String GOOGLE_REGISTRATION_ID = "google";
  private static final Map<String, Function<Map<String, Object>, OAuth2Attributes>> ATTRIBUTES_BY_PROVIDER =
      new HashMap<String, Function<Map<String, Object>, OAuth2Attributes>>() {{
        put(GOOGLE_REGISTRATION_ID, OAuth2Attributes::ofGoogleAttributes);
      }};

  private final String email;
  private final String nickname;
  private final Map<String, Object> attributes;

  private Member member;

  @Builder
  private OAuth2Attributes(String email, String nickname, Map<String, Object> attributes) {
    this.email = email;
    this.nickname = nickname;
    this.attributes = attributes;
  }

  public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
    return ATTRIBUTES_BY_PROVIDER.get(registrationId).apply(attributes);
  }

  private static OAuth2Attributes ofGoogleAttributes(Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
        .email((String) attributes.get("email"))
        .nickname((String) attributes.get("name"))
        .attributes(attributes)
        .build();
  }

  public Member toEntity(String registrationId) {
    if (member != null)
      return member;

    Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    String defaultRandomPassword = RandomStringUtils.randomAlphanumeric(10);
    String encodedRandomPassword = passwordEncoder.encode(defaultRandomPassword);

    member = Member.builder()
        .email(email)
        .nickname(nickname)
        .password(encodedRandomPassword)
        .loginType(LoginType.valueOf(registrationId.toUpperCase()))
        .build();

    return member;
  }
}
