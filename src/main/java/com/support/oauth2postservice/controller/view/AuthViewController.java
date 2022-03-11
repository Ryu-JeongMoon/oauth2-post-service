package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.service.AuthService;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

  private final AuthService authService;

  @GetMapping(UriConstants.Mapping.LOGOUT)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);

    return UriConstants.Keyword.REDIRECT + UriConstants.Mapping.ROOT;
  }
}
