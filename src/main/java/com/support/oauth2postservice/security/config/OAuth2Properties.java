package com.support.oauth2postservice.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("spring.security.oauth2.client")
public class OAuth2Properties {

  private final Map<Client, Resource> registration;

  enum Client {
    GOOGLE,
    NAVER
  }

  @Getter
  @ConstructorBinding
  @RequiredArgsConstructor
  static class Resource {

    private final String clientName;
    private final String clientId;
    private final String clientSecret;
  }
}
