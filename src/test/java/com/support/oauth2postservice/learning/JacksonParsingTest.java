package com.support.oauth2postservice.learning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class JacksonParsingTest {

  @Test
  @DisplayName("Jackson Mapping")
  void parse() throws JsonProcessingException {
    String json = " {\"access_date_time\": \"2019-10-10T11:14:16Z\", \"ip\": \"175.242.91.54\", \"username\": \"panda\"}";

    ObjectMapper objectMapper = new ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new JavaTimeModule());

    AccessLog accessLog = objectMapper.readValue(json, AccessLog.class);

    assertThat(accessLog.getAccessDateTime()).isEqualTo("2019-10-10T11:14:16Z");
    assertThat(accessLog.getIp()).isEqualTo("175.242.91.54");
    assertThat(accessLog.getUsername()).isEqualTo("panda");
  }
}
