package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostRepositoryCustom {

  Optional<PostReadResponse> findActiveToResponse(String id);

  Page<PostReadResponse> search(PostSearchRequest searchCondition);
}
