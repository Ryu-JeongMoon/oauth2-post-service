package com.support.oauth2postservice.security.config;

import com.support.oauth2postservice.security.oauth2.CustomOAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = OAuth2Properties.class)
public class OAuth2Config {

  private final OAuth2Properties oAuth2Properties;

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    List<ClientRegistration> registrations = oAuth2Properties.getRegistration()
        .keySet()
        .stream()
        .map(this::getRegistration)
        .collect(Collectors.toList());

    return new InMemoryClientRegistrationRepository(registrations);
  }

  private ClientRegistration getRegistration(OAuth2Properties.Client client) {
    OAuth2Properties.Resource resource = oAuth2Properties.getRegistration().get(client);
    String clientName = resource.getClientName();

    return CustomOAuth2Provider.caseInsensitiveValueOf(clientName)
        .getBuilder(clientName)
        .clientId(resource.getClientId())
        .clientSecret(resource.getClientSecret())
        .build();
  }
}
