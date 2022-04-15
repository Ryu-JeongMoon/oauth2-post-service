package com.support.oauth2postservice.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("application.elliptic-curve")
public class EllipticCurveProperties {

  private final String secretKey;
}
