package com.support.oauth2postservice.controller;

import com.support.oauth2postservice.security.config.JwtSecurityConfig;
import com.support.oauth2postservice.security.config.OAuth2Config;
import com.support.oauth2postservice.security.config.SecurityConfig;
import com.support.oauth2postservice.security.jwt.TokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuth2Controller.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        SecurityConfig.class, OAuth2Config.class, JwtSecurityConfig.class, TokenAuthenticationFilter.class
    })
})
class OAuth2ControllerTest extends AbstractWebMvcTest {

  @MockBean
  TokenVerifier tokenVerifier;
  @MockBean
  ClientRegistrationRepository clientRegistrationRepository;

  @Disabled("TODO - 토큰 받아오는 방식 변경으로 인한 스킵")
  @Test
  @WithMockUser
  @DisplayName("OAuth2 Access Token 반환 성공")
  void oauth2Callback() throws Exception {
    mockMvc.perform(
            get(UriConstants.Mapping.OAUTH2_CALLBACK.replace("{registrationId}", "google"))
                .param("code", "1234")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("scope").exists())
        .andExpect(jsonPath("token_type").exists())
        .andExpect(jsonPath("expires_in").exists())
        .andExpect(jsonPath("access_token").exists())
        .andExpect(jsonPath("refresh_token").exists())
        .andDo(print());
  }
}