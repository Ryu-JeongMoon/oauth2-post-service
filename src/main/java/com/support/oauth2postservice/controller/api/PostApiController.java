package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;


}
