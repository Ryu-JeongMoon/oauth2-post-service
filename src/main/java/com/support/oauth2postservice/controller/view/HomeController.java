package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping(UriConstants.Mapping.LOGIN)
  @PreAuthorize(SpELConstants.ANONYMOUS_ONLY)
  public String loginPage() {
    return "login";
  }
}
