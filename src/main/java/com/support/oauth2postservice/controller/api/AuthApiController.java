package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.service.AuthService;
import com.support.oauth2postservice.service.dto.request.LoginRequest;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

  private final AuthService authService;

  @PostMapping(UriConstants.Mapping.LOGIN)
  @PreAuthorize(SpELConstants.ANONYMOUS_ONLY)
  public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    authService.login(loginRequest, response);
    return ResponseEntity.ok().build();
  }
}
