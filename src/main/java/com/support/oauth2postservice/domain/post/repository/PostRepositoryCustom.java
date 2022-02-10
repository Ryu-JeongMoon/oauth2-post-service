package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<PostReadResponse> findActiveToResponse(String id);
}
