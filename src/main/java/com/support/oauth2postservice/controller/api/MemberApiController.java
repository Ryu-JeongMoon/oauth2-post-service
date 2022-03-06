package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.service.member.MemberService;
import com.support.oauth2postservice.service.member.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.member.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
