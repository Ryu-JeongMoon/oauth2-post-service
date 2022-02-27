package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.service.post.PostService;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.util.wrapper.WebClientWrappable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

  private final PostService postService;
  private final WebClientWrappable webClientWrappable;

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

  @GetMapping("/error-please1")
  public void errorBomb() {
    throw new IllegalArgumentException("YAHOO ~~~~~");
  }

  @GetMapping("/error-please2")
  public void errorBomb2() {
    throw new IllegalStateException("HOORAY ~~~~~");
  }

  @GetMapping("/error-please3")
  public void errorBomb3() {
    throw new ConstraintViolationException("HOORAY ~~~~~", null);
  }

  @GetMapping("/error-please4")
  public void errorBomb4() {
    throw new BadCredentialsException("HOORAY ~~~~~");
  }

  @GetMapping("/error-please5")
  public void errorBomb5() {
    throw new AccessDeniedException("HOORAY ~~~~~");
  }

  @GetMapping("/error-please6")
  public void errorBomb6() throws Exception {
    throw new Exception("HOORAY ~~~~~");
  }

  @GetMapping("/post")
  @ResponseBody
  public Page<PostReadResponse> postTest(PostSearchRequest postSearchRequest) {
    log.info("postSearchRequest = {}", postSearchRequest);
    return postService.searchByCondition(postSearchRequest);
  }

  @GetMapping("/code")
  public String validate(@RequestParam String token) {
    return String.valueOf(webClientWrappable.validateByOAuth2(token));
  }
}
