package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.security.jwt.LocalTokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.OAuth2TokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.TokenAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;
  private final LocalTokenAuthenticationFilter localTokenAuthenticationFilter;
  private final OAuth2TokenAuthenticationFilter oAuth2TokenAuthenticationFilter;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(oAuth2TokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(localTokenAuthenticationFilter, OAuth2TokenAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(tokenAuthenticationEntryPoint);
  }
}
