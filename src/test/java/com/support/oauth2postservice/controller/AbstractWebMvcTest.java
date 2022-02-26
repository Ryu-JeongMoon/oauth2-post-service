package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.helper.MockWebClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractWebMvcTest {

  @Autowired
  protected MockMvc mockMvc;

  @SpyBean
  protected MockWebClientWrapper mockWebClientWrapper;
}
