package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import com.support.oauth2postservice.util.exception.AjaxAccessDeniedException;
import com.support.oauth2postservice.util.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @PostMapping(UriConstants.Mapping.MEMBERS)
  @PreAuthorize(SpELConstants.ANONYMOUS_OR_ADMIN)
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
