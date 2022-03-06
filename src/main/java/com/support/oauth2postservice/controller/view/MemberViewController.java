package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
  public String getMembers(@Valid MemberSearchRequest memberSearchRequest, Model model) {
    Page<MemberReadResponse> memberReadResponses = memberService.searchByCondition(memberSearchRequest);

    model.addAttribute("memberReadResponses", memberReadResponses);
    return "member/list";
  }

  @GetMapping(UriConstants.Mapping.MEMBERS_SINGLE)
  public String getMember(@PathVariable String id, Model model) {
    MemberReadResponse memberReadResponse = memberService.findActiveMemberById(id);

    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/my-page";
  }

  @GetMapping(UriConstants.Mapping.MY_PAGE)
  public String myPage(Model model) {
    String id = SecurityUtils.getIdFromCurrentUser();
    MemberReadResponse memberReadResponse = memberService.findActiveMemberById(id);

    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/my-page";
  }

  @GetMapping(UriConstants.Mapping.EDIT_PAGE)
  public String editPage(@RequestParam String id, Model model) {
    MemberReadResponse memberReadResponse = memberService.findActiveMemberById(id);

    MemberEditRequest memberEditRequest = MemberEditRequest.builder()
        .nickname(memberReadResponse.getNickname())
        .role(memberReadResponse.getRole())
        .status(memberReadResponse.getStatus())
        .build();

    model.addAttribute("memberEditRequest", memberEditRequest);
    model.addAttribute("memberReadResponse", memberReadResponse);
    return "member/edit-page";
  }
}
