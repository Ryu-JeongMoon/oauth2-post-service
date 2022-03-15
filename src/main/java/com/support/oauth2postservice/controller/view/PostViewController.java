package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostViewController {

  private final PostService postService;

  @GetMapping(UriConstants.Mapping.POSTS)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String getPosts(@Valid PostSearchRequest searchRequest, Model model) {
    Page<PostReadResponse> postReadResponses = postService.searchByCondition(searchRequest);
    model.addAttribute("postReadResponses", postReadResponses);
    return "post/list";
  }

  @GetMapping(UriConstants.Mapping.POSTS_DETAIL)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String getPost() {

    return "post/detail";
  }
}
