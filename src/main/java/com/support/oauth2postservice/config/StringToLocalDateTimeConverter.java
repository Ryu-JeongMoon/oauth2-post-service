package com.support.oauth2postservice.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Nullable
  @Override
  public LocalDateTime convert(@NotNull String source) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    return LocalDateTime.parse(source, formatter);
  }
}
