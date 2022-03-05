package com.support.oauth2postservice.domain.repository;

import com.support.oauth2postservice.service.member.dto.request.MemberSearchRequest;
import com.support.oauth2postservice.service.member.dto.response.MemberReadResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MemberRepositoryCustom {

  Optional<MemberReadResponse> findActiveToResponse(String id);

  Page<MemberReadResponse> search(MemberSearchRequest memberSearchRequest);
}
