package com.support.oauth2postservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Slf4j
@Disabled
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  protected WebTestClient setUpWebTestClient() {
    return MockMvcWebTestClient
        .bindTo(mockMvc)
        .filter(logRequest())
        .build();
  }

  private ExchangeFilterFunction logRequest() {
    return (clientRequest, next) -> {
      log.info("Request: method => {},  url => {}", clientRequest.method(), clientRequest.url());
      return next.exchange(clientRequest);
    };
  }
}
