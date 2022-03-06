package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

  private final PostService postService;

  @GetMapping("/hi")
  @ResponseBody
  @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
  public String hi(OAuth2AuthenticationToken token) {
    return token.toString();
  }

  @GetMapping
  public String home() {
    return "home";
  }

  @GetMapping("/error-please")
  public void errorBomb() {
    throw new IllegalArgumentException("YAHOO ~~~~~");
  }

  @GetMapping("/post")
  @ResponseBody
  public Page<PostReadResponse> postTest(PostSearchRequest postSearchRequest) {
    log.info("postSearchRequest = {}", postSearchRequest);
    return postService.searchByCondition(postSearchRequest);
  }

  @GetMapping("/user-only")
  @ResponseBody
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public String userOnly() {
    return "user";
  }

  @GetMapping("/manager-only")
  @ResponseBody
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public String managerOnly() {
    return "manager";
  }

  @GetMapping("/admin-only")
  @ResponseBody
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public String adminOnly() {
    return "admin";
  }
}
