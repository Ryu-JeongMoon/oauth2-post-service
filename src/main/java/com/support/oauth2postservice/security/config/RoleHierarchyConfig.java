package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.domain.enumeration.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;

@Configuration
public class RoleHierarchyConfig {

  @Bean
  public AccessDecisionVoter<?> accessDecisionVoter() {
    return new RoleHierarchyVoter(roleHierarchy());
  }

  @Bean
  public RoleHierarchyImpl roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(getHierarchyFormat());
    return roleHierarchy;
  }

  private String getHierarchyFormat() {
    return Role.ADMIN.getKey() + " > " + Role.MANAGER.getKey() + "\n" +
        Role.MANAGER.getKey() + " > " + Role.USER.getKey();
  }
}

