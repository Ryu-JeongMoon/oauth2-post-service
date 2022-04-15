package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberViewController {

  private final MemberService memberService;

  @GetMapping(UriConstants.Mapping.MEMBERS)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public String getMembers(@Valid MemberSearchRequest memberSearchRequest, Model model) {
    Page<MemberReadResponse> memberReadResponses = memberService.searchByCondition(memberSearchRequest);

    model.addAttribute("memberReadResponses", memberReadResponses);
    return "member/list";
  }

  @GetMapping(UriConstants.Mapping.MEMBERS_DETAIL)
  @PreAuthorize("@roleChecker.isAuthorized(#id)")
  public String getMember(@PathVariable String id, Model model) {
    MemberReadResponse memberReadResponse = memberService.findResponseById(id);

    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/detail";
  }

  @GetMapping(UriConstants.Mapping.MEMBERS_MY_PAGE)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String myPage(Model model) {
    UserPrincipal currentUser = SecurityUtils.getPrincipalFromCurrentUser();
    MemberReadResponse memberReadResponse = memberService.findResponseByEmail(currentUser.getEmail());

    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/detail";
  }

  @GetMapping(UriConstants.Mapping.MEMBERS_EDIT)
  @PreAuthorize("@roleChecker.isAuthorized(#id)")
  public String editPage(@RequestParam String id, Model model) {
    MemberReadResponse memberReadResponse = memberService.findResponseById(id);

    MemberEditRequest memberEditRequest = MemberEditRequest.builder()
        .nickname(memberReadResponse.getNickname())
        .role(memberReadResponse.getRole())
        .status(memberReadResponse.getStatus())
        .build();

    model.addAttribute("memberEditRequest", memberEditRequest);
    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/edit";
  }
}