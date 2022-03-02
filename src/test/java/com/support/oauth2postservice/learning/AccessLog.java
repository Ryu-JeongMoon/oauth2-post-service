package com.support.oauth2postservice.learning;

import lombok.Getter;

import java.beans.ConstructorProperties;
import java.time.Instant;

@Getter
public class AccessLog {

  private final Instant accessDateTime;

  private final String ip;

  private final String username;

  @ConstructorProperties({"access_date_time", "ip", "username"})
  public AccessLog(Instant accessDateTime, String ip, String username) {

    this.accessDateTime = accessDateTime;
    this.ip = ip;
    this.username = username;
  }
}

/*
JsonCreator 로 생성 시 @JsonProperty 모든 속성 마다 매번 지정
@JsonCreator
public AccessLog(@JsonProperty("access_date_time") Instant accessDateTime,
                 @JsonProperty("ip") String ip,
                 @JsonProperty("username") String username) {

@ConstructorProperties({"access_date_time", "ip", "username"})
jdk1.6부터 제공하는 java.beans 의 annotation 활용, 직접 바인딩할 프로퍼티명 설정
 */