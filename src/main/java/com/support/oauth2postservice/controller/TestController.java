package com.support.oauth2postservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  @GetMapping("/hi")
  @ResponseBody
  @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
  public String hi(OAuth2AuthenticationToken token) {
    return token.toString();
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping
  public String home() {
    return "home";
  }
}
