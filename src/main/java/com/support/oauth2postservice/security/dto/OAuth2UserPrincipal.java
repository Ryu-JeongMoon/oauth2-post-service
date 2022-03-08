package com.support.oauth2postservice.security.dto;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class OAuth2UserPrincipal implements OAuth2User, OidcUser {

  private final String id;
  private final String email;
  private final Status status;
  private final OAuth2Token oAuth2Token;
  private final OidcIdToken oidcIdToken;
  private final Map<String, Object> claims;
  private final Map<String, Object> attributes;
  private final Collection<? extends GrantedAuthority> authorities;

  public static OAuth2UserPrincipal from(Member member) {
    return new OAuth2UserPrincipal(
        member.getId(),
        member.getEmail(),
        member.getStatus(),
        Jwt.withTokenValue("NOT_VALID")
            .header(TokenConstants.BEARER_TYPE, TokenConstants.BEARER_TYPE)
            .issuer(TokenConstants.OAUTH2_GOOGLE_TOKEN_ISSUER)
            .build(),
        OidcIdToken.withTokenValue("NOT_VALID").issuer(TokenConstants.OAUTH2_GOOGLE_TOKEN_ISSUER).build(),
        Collections.emptyMap(),
        Collections.emptyMap(),
        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()))
    );
  }

  public static OAuth2UserPrincipal of(Member member, OAuth2User oAuth2User, OAuth2Token oAuth2Token) {
    if (oAuth2User == null)
      return null;

    if (oAuth2User instanceof OidcUser) {
      OidcUser oidcUser = (OidcUser) oAuth2User;
      return toOidcUserPrincipal(member, oidcUser, oAuth2Token);
    }

    return toOAuth2UserPrincipal(member, oAuth2User, oAuth2Token);
  }

  private static OAuth2UserPrincipal toOAuth2UserPrincipal(Member member, OAuth2User oAuth2User, OAuth2Token oAuth2Token) {
    return new OAuth2UserPrincipal(
        member.getId(),
        member.getEmail(),
        member.getStatus(),
        oAuth2Token,
        OidcIdToken.withTokenValue("").build(),
        Collections.emptyMap(),
        oAuth2User.getAttributes(),
        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()))
    );
  }

  private static OAuth2UserPrincipal toOidcUserPrincipal(Member member, OidcUser oidcUser, OAuth2Token oAuth2Token) {
    return new OAuth2UserPrincipal(
        member.getId(),
        member.getEmail(),
        member.getStatus(),
        oAuth2Token,
        oidcUser.getIdToken(),
        oidcUser.getClaims(),
        oidcUser.getAttributes(),
        Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()))
    );
  }

  public Authentication toAuthentication() {
    return new UsernamePasswordAuthenticationToken(
        this,
        "",
        this.authorities
    );
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(authorities);
  }

  @Override
  public String getName() {
    return email;
  }

  @Override
  public Map<String, Object> getClaims() {
    return Collections.unmodifiableMap(claims);
  }

  @Override
  public OidcUserInfo getUserInfo() {
    throw new UnsupportedOperationException(ExceptionMessages.Common.UNSUPPORTED_OPERATION);
  }

  @Override
  public OidcIdToken getIdToken() {
    return oidcIdToken;
  }
}
