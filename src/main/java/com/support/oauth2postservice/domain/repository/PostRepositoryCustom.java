package com.support.oauth2postservice.domain.repository;

import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostRepositoryCustom {

  Optional<PostReadResponse> findResponseById(String id);

  Page<PostReadResponse> search(PostSearchRequest searchCondition);
}
