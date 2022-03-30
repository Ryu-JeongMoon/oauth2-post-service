package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
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

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostViewController {

  private final PostService postService;

  @GetMapping(UriConstants.Mapping.POSTS)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String getPosts(@Valid PostSearchRequest searchRequest, Model model) {
    Role roleFromCurrentUser = SecurityUtils.getRoleFromCurrentUser();
    Page<PostReadResponse> postReadResponses = postService.searchByCondition(searchRequest, roleFromCurrentUser);
    model.addAttribute("postReadResponses", postReadResponses);
    return "post/list";
  }

  @GetMapping(UriConstants.Mapping.POSTS_DETAIL)
  @PreAuthorize(SpELConstants.ANY_ROLE_ALLOWED)
  public String getPost(@PathVariable String id, Model model) {
    PostReadResponse postReadResponse = postService.findById(id);
    model.addAttribute("postReadResponse", postReadResponse);
    return "post/detail";
  }

  @GetMapping(UriConstants.Mapping.POSTS_WRITE_PAGE)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public String writePage(Model model) {
    String memberId = SecurityUtils.getPrincipalFromCurrentUser().getId();
    model.addAttribute("postCreateRequest", PostCreateRequest.withMemberId(memberId));
    return "post/write";
  }

  @GetMapping(UriConstants.Mapping.POSTS_EDIT)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public String editPage(@PathVariable String id, Model model) {
    PostReadResponse postReadResponse = postService.findById(id);

    PostEditRequest postEditRequest = PostEditRequest.builder()
        .title(postReadResponse.getTitle())
        .content(postReadResponse.getContent())
        .openedAt(postReadResponse.getOpenedAt())
        .build();

    model.addAttribute("postEditRequest", postEditRequest);
    model.addAttribute("postReadResponse", postReadResponse);
    return "post/edit";
  }
}
