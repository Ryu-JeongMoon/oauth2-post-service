package com.support.oauth2postservice.controller.api;

import com.support.oauth2postservice.controller.AbstractWebMvcTest;
import com.support.oauth2postservice.helper.MockWebClientWrapper;
import com.support.oauth2postservice.security.config.JwtSecurityConfig;
import com.support.oauth2postservice.security.config.OAuth2Config;
import com.support.oauth2postservice.security.config.SecurityConfig;
import com.support.oauth2postservice.security.jwt.TokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.TokenVerifier;
import com.support.oauth2postservice.util.constant.TokenConstants;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuth2Controller.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        SecurityConfig.class, JwtSecurityConfig.class, TokenAuthenticationFilter.class
    })
})
@Import(OAuth2Config.class)
class OAuth2ControllerTest extends AbstractWebMvcTest {

  @MockBean
  TokenVerifier tokenVerifier;

  @SpyBean
  MockWebClientWrapper mockWebClientWrapper;

  @Nested
  @DisplayName("Access Token 요청")
  class AccessTokenRequestTest {

    @Test
    @WithMockUser
    @DisplayName("성공 - OAuth2TokenResponse 반환")
    void getOAuth2Token() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.ISSUE_TOKEN.replace("{registrationId}", "google"))
                  .queryParam(TokenConstants.CODE, "1234")
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath(TokenConstants.SCOPE).exists())
          .andExpect(jsonPath(TokenConstants.TOKEN_TYPE).exists())
          .andExpect(jsonPath(TokenConstants.EXPIRES_IN).exists())
          .andExpect(jsonPath(TokenConstants.ID_TOKEN).exists())
          .andExpect(jsonPath(TokenConstants.ACCESS_TOKEN).exists())
          .andExpect(jsonPath(TokenConstants.REFRESH_TOKEN).exists())
          .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("실패 - 빈 Code 로 요청 시 빈 TokenResponse 반환")
    void getOAuth2Token_failByEmptyCode() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.ISSUE_TOKEN.replace("{registrationId}", "google"))
                  .queryParam(TokenConstants.CODE, "")
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath(TokenConstants.SCOPE).isEmpty())
          .andExpect(jsonPath(TokenConstants.TOKEN_TYPE).isEmpty())
          .andExpect(jsonPath(TokenConstants.EXPIRES_IN).isEmpty())
          .andExpect(jsonPath(TokenConstants.ID_TOKEN).isEmpty())
          .andExpect(jsonPath(TokenConstants.ACCESS_TOKEN).isEmpty())
          .andExpect(jsonPath(TokenConstants.REFRESH_TOKEN).isEmpty())
          .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("실패 - code query-string 없을 시 MissingServletRequestParameterException 발생")
    void getOAuth2Token_failByEmptyQueryString() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.ISSUE_TOKEN.replace("{registrationId}", "google"))
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("exception").exists())
          .andExpect(jsonPath("message").exists())
          .andDo(print());
    }
  }

  @Nested
  @DisplayName("ID Token 검증")
  class IdTokenValidationTest {

    @Test
    @WithMockUser
    @DisplayName("성공 - true 반환")
    void validate() throws Exception {
      MvcResult result = mockMvc.perform(
              get(UriConstants.Mapping.VALIDATE_TOKEN)
                  .queryParam(TokenConstants.ID_TOKEN, "yahoo~~")
          )
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();

      boolean isValid = BooleanUtils.toBoolean(result.getResponse().getContentAsString());
      assertThat(isValid).isTrue();
    }

    @Test
    @WithMockUser
    @DisplayName("실패 - 빈 ID Token 면 false 반환")
    void validate_failByEmptyIdToken() throws Exception {
      MvcResult result = mockMvc.perform(
              get(UriConstants.Mapping.VALIDATE_TOKEN)
                  .queryParam(TokenConstants.ID_TOKEN, "")
          )
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();

      boolean isValid = BooleanUtils.toBoolean(result.getResponse().getContentAsString());
      assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("실패 - 권한 없을 시 로그인 페이지 리다이렉트")
    void validate_failByAuthority() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.VALIDATE_TOKEN)
                  .queryParam(TokenConstants.ID_TOKEN, "yahoo~~")
          )
          .andExpect(status().is3xxRedirection())
          .andDo(print());
    }
  }

  @Nested
  @DisplayName("Access Token 재발급")
  class AccessTokenRenewalTest {

    @Test
    @WithMockUser
    @DisplayName("성공 - OAuth2TokenResponse 반환")
    void renew() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.RENEW_TOKEN.replace("{registrationId}", "google"))
                  .queryParam(TokenConstants.REFRESH_TOKEN, "panda")
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath(TokenConstants.SCOPE).exists())
          .andExpect(jsonPath(TokenConstants.TOKEN_TYPE).exists())
          .andExpect(jsonPath(TokenConstants.EXPIRES_IN).exists())
          .andExpect(jsonPath(TokenConstants.ID_TOKEN).exists())
          .andExpect(jsonPath(TokenConstants.ACCESS_TOKEN).exists())
          .andExpect(jsonPath(TokenConstants.REFRESH_TOKEN).exists())
          .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("등록되지 않은 Registration - HTTP STATUS 500 반환")
    void renew_failByWrongUrl() throws Exception {
      mockMvc.perform(
              get(UriConstants.Mapping.RENEW_TOKEN.replace("{registrationId}", "facebook"))
                  .queryParam(TokenConstants.REFRESH_TOKEN, "panda")
          )
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("exception").hasJsonPath())
          .andExpect(jsonPath("message").hasJsonPath())
          .andDo(print());
    }
  }
}