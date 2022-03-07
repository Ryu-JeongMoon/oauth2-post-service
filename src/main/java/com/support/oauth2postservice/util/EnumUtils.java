package com.support.oauth2postservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumUtils {

  public static <E extends Enum<E>> String[] toStringArray(E[] e) {
    return Arrays.stream(e)
        .map(Enum::name)
        .toArray(String[]::new);
  }
}
