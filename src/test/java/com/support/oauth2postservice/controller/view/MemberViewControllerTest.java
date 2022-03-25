package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.controller.AbstractWebMvcTest;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PageableTestHelper;
import com.support.oauth2postservice.helper.PrincipalTestHelper;
import com.support.oauth2postservice.security.config.JwtSecurityConfig;
import com.support.oauth2postservice.security.config.SecurityConfig;
import com.support.oauth2postservice.security.jwt.LocalTokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.OAuth2TokenAuthenticationFilter;
import com.support.oauth2postservice.service.MemberService;
import com.support.oauth2postservice.service.dto.response.MemberReadResponse;
import com.support.oauth2postservice.util.SecurityUtils;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = MemberViewController.class,
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
@Import(DefaultWebSecurityExpressionHandler.class)
class MemberViewControllerTest extends AbstractWebMvcTest {

  @MockBean
  MemberService memberService;

  WebTestClient webTestClient;

  MemberReadResponse readResponse;

  @BeforeEach
  void setUp() {
    Authentication authentication = PrincipalTestHelper.createUserPrincipal().toAuthentication();
    SecurityUtils.setAuthentication(authentication);

    readResponse = MemberTestHelper.createDefaultResponse();

    webTestClient = MockMvcWebTestClient
        .bindTo(mockMvc)
        .defaultHeader("panda", "bear")
        .filter(logRequest())
        .build();
  }

  private ExchangeFilterFunction logRequest() {
    return (clientRequest, next) -> {
      System.out.printf("Request: %s %s %n", clientRequest.method(), clientRequest.url());
      return next.exchange(clientRequest);
    };
  }

  @Test
  @DisplayName("수정 페이지 조회")
  void editPage() throws Exception {
    Mockito.when(memberService.findResponseById(anyString())).thenReturn(readResponse);

    EntityExchangeResult<byte[]> result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(UriConstants.Mapping.MEMBERS_EDIT)
            .queryParam("id", "1")
            .build())
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody().returnResult();

    MockMvcWebTestClient.resultActionsFor(result)
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            model().attributeExists("memberEditRequest"),
            model().attributeExists("memberReadResponse"),
            view().name("member/edit")
        );
  }

  @Nested
  @DisplayName("회원 목록 조회")
  class GetMembersTest {

    @Test
    @DisplayName("검색 조건 없는 조회")
    void getMembers() throws Exception {
      List<MemberReadResponse> result = Collections.singletonList(readResponse);
      PageImpl<MemberReadResponse> readResponses = new PageImpl<>(result, PageableTestHelper.createDefault(), result.size());
      Mockito.when(memberService.searchByCondition(any())).thenReturn(readResponses);

      mockMvc.perform(
              get(UriConstants.Mapping.MEMBERS)
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              model().attributeExists("memberReadResponses"),
              view().name("member/list")
          );
    }

    @Test
    @DisplayName("검색 조건 있는 조회")
    void getMembers_successWithSearchRequest() throws Exception {
      List<MemberReadResponse> result = Collections.singletonList(readResponse);
      PageImpl<MemberReadResponse> readResponses = new PageImpl<>(result, PageableTestHelper.createDefault(), result.size());
      Mockito.when(memberService.searchByCondition(any())).thenReturn(readResponses);

      mockMvc.perform(
              get(UriConstants.Mapping.MEMBERS)
                  .queryParam("email", "panda")
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              model().attributeExists("memberReadResponses"),
              view().name("member/list")
          );
    }

    @Test
    @DisplayName("검색 조건 있는 조회 - WebTestClient 이용")
    void getMembers_successWithSearchRequestByWebTestClient() throws Exception {
      List<MemberReadResponse> result = Collections.singletonList(readResponse);
      PageImpl<MemberReadResponse> readResponses = new PageImpl<>(result, PageableTestHelper.createDefault(), result.size());
      Mockito.when(memberService.searchByCondition(any())).thenReturn(readResponses);

      EntityExchangeResult<byte[]> exchangeResult = webTestClient.get()
          .uri(UriConstants.Mapping.MEMBERS)
          .exchange()
          .expectStatus().is2xxSuccessful()
          .expectBody().returnResult();

      MockMvcWebTestClient.resultActionsFor(exchangeResult)
          .andExpectAll(
              model().attributeExists("memberReadResponses"),
              view().name("member/list")
          );
    }
  }

  @Nested
  @DisplayName("회원 상세 조회")
  class GetMemberTest {

    @Test
    @DisplayName("성공")
    void getMember() throws Exception {
      Mockito.when(memberService.findResponseById(any())).thenReturn(readResponse);

      EntityExchangeResult<byte[]> result = webTestClient.get()
          .uri(UriConstants.Mapping.MEMBERS_DETAIL.replace("{id}", "1"))
          .exchange()
          .expectStatus().is2xxSuccessful()
          .expectBody().returnResult();

      MockMvcWebTestClient.resultActionsFor(result)
          .andDo(print())
          .andExpectAll(
              view().name("member/detail"),
              model().attributeExists("memberReadResponse")
          );
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 회원")
    void getMember_failBy() throws Exception {
      Mockito.when(memberService.findResponseById(anyString())).thenThrow(IllegalArgumentException.class);

      EntityExchangeResult<byte[]> result = webTestClient.get()
          .uri(UriConstants.Mapping.MEMBERS_DETAIL.replace("{id}", "1"))
          .exchange()
          .expectStatus().is4xxClientError()
          .expectBody().returnResult();

      MockMvcWebTestClient.resultActionsFor(result)
          .andDo(print())
          .andExpectAll(
              status().isBadRequest()
          );
    }
  }

  @Nested
  @DisplayName("마이페이지 조회")
  class MyPageTest {

    @Test
    @DisplayName("성공")
    void myPage() throws Exception {
      Mockito.when(memberService.findResponseById(anyString())).thenReturn(readResponse);

      EntityExchangeResult<byte[]> result = webTestClient.get()
          .uri(UriConstants.Mapping.MEMBERS_MY_PAGE)
          .exchange()
          .expectStatus().is2xxSuccessful()
          .expectBody().returnResult();

      MockMvcWebTestClient.resultActionsFor(result)
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              model().attributeExists("memberReadResponse"),
              view().name("member/detail")
          );
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 회원")
    void myPage_failByNotExistsId() throws Exception {
      Mockito.when(memberService.findResponseById(anyString())).thenThrow(IllegalArgumentException.class);

      EntityExchangeResult<byte[]> result = webTestClient.get()
          .uri(UriConstants.Mapping.MEMBERS_MY_PAGE)
          .exchange()
          .expectStatus().is4xxClientError()
          .expectBody().returnResult();

      MockMvcWebTestClient.resultActionsFor(result)
          .andDo(print())
          .andExpect(status().isBadRequest());
    }
  }
}