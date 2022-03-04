package com.support.oauth2postservice.security.dto;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

  private final String id;
  private final String email;
  private final Status status;
  private final Collection<? extends GrantedAuthority> authorities;

  public static UserPrincipal from(Member member) {
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString()));
    return UserPrincipal.of(member.getId(), member.getEmail(), authorities);
  }

  public static UserPrincipal of(String id, String email, Collection<? extends GrantedAuthority> authorities) {
    return new UserPrincipal(id, email, Status.ACTIVE, authorities);
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
  public Collection<? extends GrantedAuthority> getAuthorities() {
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
