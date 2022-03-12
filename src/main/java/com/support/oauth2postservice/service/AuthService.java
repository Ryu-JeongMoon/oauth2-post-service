package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.TokenFactory;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.service.dto.request.LoginRequest;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenFactory tokenFactory;
  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public void login(LoginRequest loginRequest, HttpServletResponse response) {
    memberRepository.findActiveByEmail(loginRequest.getEmail())
        .ifPresent(
            member -> {
              if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
                throw new BadCredentialsException(ExceptionMessages.Member.PASSWORD_NOT_VALID);

              Authentication authentication = UserPrincipal
                  .from(member)
                  .toAuthentication();

              SecurityUtils.setAuthentication(authentication);
              TokenResponse tokenResponse = tokenFactory.create(authentication);
              CookieUtils.addLocalTokenToBrowser(response, tokenResponse);
            });
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextHolder.clearContext();
    CookieUtils.deleteCookie(request, response, TokenConstants.ID_TOKEN);
  }
}
