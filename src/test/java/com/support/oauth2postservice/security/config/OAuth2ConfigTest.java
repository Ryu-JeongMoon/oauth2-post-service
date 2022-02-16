package com.support.oauth2postservice.security.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootTest(classes = OAuth2Config.class)
class OAuth2ConfigTest {

    @Autowired
    OAuth2Config oAuth2Config;

    @Test
    @DisplayName("부트 시작 과정에 동적으로 설정에서 정보를 읽어온다")
    void clientRegistrationRepository() {
        ClientRegistrationRepository clientRegistrationRepository = oAuth2Config.clientRegistrationRepository();
        ClientRegistration registrationId = clientRegistrationRepository
                .findByRegistrationId(OAuth2Properties.Client.GOOGLE.name().toLowerCase());

        Assertions.assertThat(registrationId.getClientName()).isNotBlank();
    }
}