package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import com.support.oauth2postservice.util.exception.AjaxAccessDeniedException;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @PostMapping(value = UriConstants.Mapping.MEMBERS)
  @PreAuthorize(SpELConstants.ANONYMOUS_OR_ADMIN)
  @Operation(summary = "회원 가입", description = "애플리케이션 자체 회원 가입")
  @Parameters({
      @Parameter(name = ColumnConstants.Name.EMAIL, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @Parameter(name = ColumnConstants.Name.NICKNAME, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
      @Parameter(name = ColumnConstants.Name.PASSWORD, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  public ResponseEntity<Void> join(@RequestBody @Valid MemberSignupRequest memberSignupRequest) {
    memberService.join(memberSignupRequest);

    URI uri = URI.create(UriConstants.Full.MY_PAGE);
    return ResponseEntity.created(uri).build();
  }

  @PatchMapping(UriConstants.Mapping.MEMBERS_EDIT)
  @PreAuthorize("@roleChecker.isAuthorized(#memberEditRequest.id)")
  public ResponseEntity<Void> edit(@RequestBody @Valid MemberEditRequest memberEditRequest) {
    Role currentUserRole = SecurityUtils.getRoleFromCurrentUser();
    throwIfUnauthorized(currentUserRole, memberEditRequest.getRole());

    memberService.edit(memberEditRequest);
    return ResponseEntity.ok().build();
  }

  private void throwIfUnauthorized(Role currentUserRole, Role toBeChangedRole) {
    if (currentUserRole.isInferiorThan(toBeChangedRole))
      throw new AjaxAccessDeniedException(ExceptionMessages.Member.ACCESS_DENIED);
  }

  @DeleteMapping(UriConstants.Mapping.MEMBERS_EDIT)
  @PreAuthorize("@roleChecker.isOwner(#memberDeleteRequest.id) or " + SpELConstants.ADMIN_ONLY)
  public ResponseEntity<Void> leave(@RequestBody @Valid MemberDeleteRequest memberDeleteRequest) {
    Role roleFromCurrentUser = SecurityUtils.getRoleFromCurrentUser();
    memberService.leave(roleFromCurrentUser, memberDeleteRequest);
    return ResponseEntity.ok().build();
  }
}
