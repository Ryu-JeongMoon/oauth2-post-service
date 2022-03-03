package com.support.oauth2postservice.security.service;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.security.oauth2.OAuth2Attributes;

public interface OAuth2MemberService {

  Member getMember(String registrationId, OAuth2Attributes attributes);
}
