package com.support.oauth2postservice.config.security;

import com.support.oauth2postservice.core.security.oauth2.CustomOAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

    private final static Collection<String> CLIENTS = Arrays.asList("google", "naver");
    private final static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    private final Environment environment;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = CLIENTS.stream()
                .map(this::getRegistration)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = environment.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
        String clientSecret = environment.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

        assert clientId != null;
        return CustomOAuth2Provider.valueOf(clientId.toUpperCase())
                .getBuilder(client)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
