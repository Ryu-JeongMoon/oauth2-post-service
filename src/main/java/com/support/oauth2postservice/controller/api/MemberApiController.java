package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

  @PatchMapping(UriConstants.Mapping.EDIT_PAGE)
  public ResponseEntity<Void> edit(@RequestBody @Valid MemberEditRequest memberEditRequest) {
    memberService.edit(memberEditRequest);

    return ResponseEntity.ok().build();
  }
}
