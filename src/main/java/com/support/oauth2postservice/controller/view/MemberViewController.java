package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.service.member.MemberService;
import com.support.oauth2postservice.service.member.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberViewController {

  private final MemberService memberService;

  @GetMapping(UriConstants.Mapping.MEMBERS)
  public String getMembers(@Valid MemberSearchRequest memberSearchRequest, Model model) {
    Page<MemberReadResponse> memberReadResponses = memberService.searchByCondition(memberSearchRequest);

    model.addAttribute("memberReadResponses", memberReadResponses);
    memberReadResponses.getContent().forEach(System.out::println);
    return "member/list";
  }

  @GetMapping(UriConstants.Mapping.MEMBERS_SINGLE)
  public String getMember(@PathVariable String id) {
    return "member/my-page";
  }

  @GetMapping(UriConstants.Mapping.MY_PAGE)
  public String myPage(Model model) {
    String id = SecurityUtils.getIdFromCurrentUser();
    MemberReadResponse memberResponse = memberService.findActiveMemberById(id);

    model.addAttribute("memberResponse", memberResponse);
    return "member/my-page";
  }
}
