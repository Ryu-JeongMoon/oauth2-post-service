package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.security.jwt.TokenResponse;
import com.support.oauth2postservice.service.AuthService;
import com.support.oauth2postservice.service.RefreshTokenService;
import com.support.oauth2postservice.service.dto.request.LoginRequest;
import com.support.oauth2postservice.util.CookieUtils;
import com.support.oauth2postservice.util.SecurityUtils;
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
  private final RefreshTokenService refreshTokenService;

  @PostMapping(UriConstants.Mapping.LOGIN)
  @PreAuthorize(SpELConstants.ANONYMOUS_ONLY)
  public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    TokenResponse tokenResponse = authService.getTokenAfterLogin(loginRequest);
    refreshTokenService.saveOrUpdate(SecurityUtils.getPrincipalFromCurrentUser(), tokenResponse.getRefreshToken());

    CookieUtils.addLocalTokenToBrowser(response, tokenResponse);
    return getUriByRole();
  }

  private ResponseEntity<String> getUriByRole() {
    Role currentRole = SecurityUtils.getRoleFromCurrentUser();

    return currentRole.isInferiorThan(Role.MANAGER)
        ? ResponseEntity.ok(UriConstants.Mapping.POSTS)
        : ResponseEntity.ok(UriConstants.Mapping.MEMBERS);
  }
}
