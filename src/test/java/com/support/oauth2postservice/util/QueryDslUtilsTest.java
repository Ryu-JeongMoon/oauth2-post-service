package com.support.oauth2postservice.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryDslUtilsTest {

  @Test
  @DisplayName("")
  void equalsAnyIgnoreCase() {
    String[] strings = {"panda", "bear", "yahoo"};

    assertFalse(StringUtils.equalsAnyIgnoreCase("pand", strings));
    assertTrue(StringUtils.equalsAnyIgnoreCase("panda", strings));
  }
}