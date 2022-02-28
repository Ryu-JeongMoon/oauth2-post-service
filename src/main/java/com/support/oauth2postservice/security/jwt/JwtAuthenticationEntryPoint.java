package com.support.oauth2postservice.security.jwt;

import com.support.oauth2postservice.util.exception.ExceptionMessages;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    throw new TokenException(ExceptionMessages.Token.VALIDATION_REJECTED, e);
  }
}
