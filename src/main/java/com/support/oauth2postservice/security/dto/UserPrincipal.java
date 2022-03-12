package com.support.oauth2postservice.security.dto;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@ToString
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

  private final String id;
  private final String email;
  private final Status status;
  private final Collection<Role> authorities;

  public static UserPrincipal from(Member member) {
    return UserPrincipal.of(member.getId(), member.getEmail(), Collections.singletonList(member.getRole()));
  }

  public static UserPrincipal of(String id, String email, Collection<Role> authorities) {
    return new UserPrincipal(id, email, Status.ACTIVE, authorities);
  }

  public Authentication toAuthentication() {
    return new UsernamePasswordAuthenticationToken(this, "", this.authorities);
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public Collection<Role> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return Status.ACTIVE.equals(status);
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }
}
