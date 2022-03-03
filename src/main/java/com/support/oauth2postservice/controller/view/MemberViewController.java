package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.service.member.MemberService;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberViewController {

  private final MemberService memberService;

  @GetMapping(UriConstants.Mapping.MY_PAGE)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String myPage(Model model) {
    String currentUserEmail = SecurityUtils.getCurrentUserEmail();
    MemberReadResponse memberResponse = memberService.findActiveMemberByEmail(currentUserEmail);

    model.addAttribute("memberResponse", memberResponse);
    return "member/my-page";
  }
}
