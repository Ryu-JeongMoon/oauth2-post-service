package com.support.oauth2postservice.config.security;

import com.nhncorp.lucy.security.xss.XssSaxFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebConfigTest {

  @Test
  @DisplayName("Lucy-XSS-Servlet-Filter Test")
  void xssEscape() {
    String dirty = "<script>alert('dirty');</script>";

    String clean = XssSaxFilter.getInstance().doFilter(dirty);
    System.out.println(clean);
    clean = clean.replace("<!-- Not Allowed Tag Filtered -->", "");
    System.out.println(clean);

    assertThat(clean).isNotEqualTo(dirty);
    assertThat(clean.contains("<")).isEqualTo(false);
    assertThat(clean.contains(">")).isEqualTo(false);
    assertThat(clean.contains("&lt;")).isEqualTo(true);
    assertThat(clean.contains("&gt;")).isEqualTo(true);
  }
}