package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.support.oauth2postservice.security.service.CustomOAuth2UserService;
import com.support.oauth2postservice.security.service.CustomOidcUserService;
import com.support.oauth2postservice.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtSecurityConfig jwtSecurityConfig;
  private final CustomOidcUserService customOidcUserService;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomUserDetailsService customUserDetailsService;
  private final ClientRegistrationRepository clientRegistrationRepository;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Argon2PasswordEncoder();
  }

  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        .apply(jwtSecurityConfig)
        .and()

        .oauth2Login()
        .loginPage("/login")
        .clientRegistrationRepository(clientRegistrationRepository)
        .authorizationEndpoint()
        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)

        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .oidcUserService(customOidcUserService)

        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);
  }
}