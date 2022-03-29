package com.support.oauth2postservice.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.oauth2postservice.controller.AbstractWebMvcTest;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PrincipalTestHelper;
import com.support.oauth2postservice.security.config.JwtSecurityConfig;
import com.support.oauth2postservice.security.config.SecurityConfig;
import com.support.oauth2postservice.security.dto.UserPrincipal;
import com.support.oauth2postservice.security.jwt.LocalTokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.OAuth2TokenAuthenticationFilter;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = MemberApiController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            SecurityConfig.class, JwtSecurityConfig.class,
            LocalTokenAuthenticationFilter.class, OAuth2TokenAuthenticationFilter.class
        })
    },
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
    }
)
@WithMockUser
class MemberApiControllerTest extends AbstractWebMvcTest {

  @MockBean
  MemberService memberService;
  @Autowired
  ObjectMapper objectMapper;

  UserPrincipal userPrincipal;

  @BeforeEach
  void setUp() {
    userPrincipal = PrincipalTestHelper.createUserPrincipal();
  }

  @Nested
  @DisplayName("회원 가입")
  class JoinTest {

    @Test
    @DisplayName("성공")
    void join() throws Exception {
      MemberSignupRequest signupRequest = MemberTestHelper.createUserSignupRequest();
      String jsonValue = objectMapper.writeValueAsString(signupRequest);

      mockMvc.perform(
              post(UriConstants.Mapping.MEMBERS)
                  .content(jsonValue)
                  .contentType(MediaType.APPLICATION_JSON)
                  .with(csrf())
                  .secure(true)
          )
          .andDo(print())
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("실패")
    void join_failByValidation() throws Exception {
      MemberSignupRequest signupRequest = MemberSignupRequest.builder()
          .email("1")
          .build();

      String jsonValue = objectMapper.writeValueAsString(signupRequest);

      mockMvc.perform(
              post(UriConstants.Mapping.MEMBERS)
                  .content(jsonValue)
                  .contentType(MediaType.APPLICATION_JSON)
                  .with(csrf())
                  .secure(true)
          )
          .andDo(print())
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("회원 정보 수정")
  class EditTest {

    @Test
    @DisplayName("성공")
    void edit() throws Exception {
      Authentication authentication = userPrincipal.toAuthentication();
      SecurityContextHolder.getContext().setAuthentication(authentication);

      MemberEditRequest editRequest = MemberTestHelper.createDefaultEditRequest();
      String jsonRequest = objectMapper.writeValueAsString(editRequest);

      mockMvc.perform(
              patch(UriConstants.Mapping.MEMBERS_EDIT)
                  .content(jsonRequest)
                  .contentType(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("404 실패 - UserPrincipal 부적합")
    void edit_failByUserPrincipal() throws Exception {
      MemberEditRequest editRequest = MemberTestHelper.createDefaultEditRequest();
      String jsonRequest = objectMapper.writeValueAsString(editRequest);

      mockMvc.perform(
              patch(UriConstants.Mapping.MEMBERS_EDIT)
                  .content(jsonRequest)
                  .contentType(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("400 실패 - Validation 부적합")
    void edit_failByValidation() throws Exception {
      Authentication authentication = userPrincipal.toAuthentication();
      SecurityContextHolder.getContext().setAuthentication(authentication);

      MemberEditRequest editRequest = MemberEditRequest.builder()
          .nickname("abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd")
          .build();

      String jsonRequest = objectMapper.writeValueAsString(editRequest);

      mockMvc.perform(
              patch(UriConstants.Mapping.MEMBERS_EDIT)
                  .content(jsonRequest)
                  .contentType(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("회원 탈퇴")
  class LeaveTest {

    @Test
    @DisplayName("성공")
    void leave() throws Exception {
      Authentication authentication = userPrincipal.toAuthentication();
      SecurityContextHolder.getContext().setAuthentication(authentication);

      MemberDeleteRequest deleteRequest = MemberTestHelper.createDeleteRequest("1");
      String jsonRequest = objectMapper.writeValueAsString(deleteRequest);

      mockMvc.perform(
              delete(UriConstants.Mapping.MEMBERS_EDIT)
                  .content(jsonRequest)
                  .contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(print())
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("400 실패 - Bean Validation")
    void leave_failByValidation() throws Exception {
      Authentication authentication = userPrincipal.toAuthentication();
      SecurityContextHolder.getContext().setAuthentication(authentication);

      MemberDeleteRequest deleteRequest = MemberDeleteRequest.builder()
          .id("")
          .password("")
          .build();

      String jsonRequest = objectMapper.writeValueAsString(deleteRequest);

      mockMvc.perform(
              delete(UriConstants.Mapping.MEMBERS_EDIT)
                  .content(jsonRequest)
                  .contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(print())
          .andExpect(status().isBadRequest());
    }
  }
}