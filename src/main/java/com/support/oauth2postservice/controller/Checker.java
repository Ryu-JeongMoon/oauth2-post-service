package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class Checker {

  public boolean isOwner(String memberId) {
    String currentUserId = SecurityUtils.getIdFromCurrentUser();
    return StringUtils.equalsIgnoreCase(currentUserId, memberId);
  }
}
