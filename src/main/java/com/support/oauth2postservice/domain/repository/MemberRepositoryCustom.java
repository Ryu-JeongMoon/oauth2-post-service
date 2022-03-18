package com.support.oauth2postservice.domain.repository;

import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MemberRepositoryCustom {

  Optional<MemberReadResponse> findResponseById(String id);

  Page<MemberReadResponse> search(MemberSearchRequest memberSearchRequest);
}
