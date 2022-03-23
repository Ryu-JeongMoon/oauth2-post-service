package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.dto.request.PostEditRequest;
import com.support.oauth2postservice.util.constant.SpELConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

  @PostMapping(UriConstants.Mapping.POSTS)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public ResponseEntity<Void> write(@RequestBody @Valid PostCreateRequest postCreateRequest) {
    postService.write(postCreateRequest);

    URI uri = URI.create(UriConstants.Mapping.POSTS);
    return ResponseEntity.created(uri).build();
  }

  @PatchMapping(UriConstants.Mapping.POSTS_EDIT)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public ResponseEntity<Void> edit(@PathVariable String id, @RequestBody @Valid PostEditRequest postEditRequest) {
    postService.edit(id, postEditRequest);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(UriConstants.Mapping.POSTS_DETAIL)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public ResponseEntity<Void> close(@PathVariable String id) {
    postService.close(id);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(UriConstants.Mapping.POSTS_DETAIL)
  @PreAuthorize(SpELConstants.MANAGER_OR_ADMIN)
  public ResponseEntity<Void> reopen(@PathVariable String id) {
    postService.reopen(id);
    return ResponseEntity.noContent().build();
  }
}
