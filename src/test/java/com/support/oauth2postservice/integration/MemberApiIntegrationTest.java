package com.support.oauth2postservice.integration;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.WithMockCustomUser;
import com.support.oauth2postservice.service.dto.request.MemberDeleteRequest;
import com.support.oauth2postservice.service.dto.request.MemberEditRequest;
import com.support.oauth2postservice.service.dto.request.MemberSignupRequest;
import com.support.oauth2postservice.util.constant.UriConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 통합 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberApiIntegrationTest extends AbstractIntegrationTest {

  WebTestClient webTestClient;

  @Autowired
  MemberRepository memberRepository;

  private Member member;

  @BeforeAll
  void setUpEarly() {
    webTestClient = setUpWebTestClient();
  }

  @BeforeEach
  void setUp() {
    Member manager = MemberTestHelper.createManger();
    member = memberRepository.save(manager);
  }

  @Test
  @WithAnonymousUser
  @DisplayName("가입")
  void join() throws Exception {
    MemberSignupRequest signupRequest = MemberTestHelper.createUserSignupRequest();
    String jsonValue = objectMapper.writeValueAsString(signupRequest);

    EntityExchangeResult<byte[]> result = webTestClient.post()
        .uri(UriConstants.Mapping.MEMBERS)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(jsonValue))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody().returnResult();

    MockMvcWebTestClient.resultActionsFor(result)
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            header().exists(HttpHeaders.LOCATION)
        );
  }

  @Test
  @DisplayName("관리자 수정")
  @WithMockCustomUser(role = Role.ADMIN)
  void edit() throws Exception {
    MemberEditRequest editRequest = MemberTestHelper.createSpecificEditRequest(member.getId());
    String jsonValue = objectMapper.writeValueAsString(editRequest);

    EntityExchangeResult<byte[]> result = webTestClient.patch()
        .uri(UriConstants.Mapping.MEMBERS_EDIT)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(jsonValue))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody().returnResult();

    MockMvcWebTestClient.resultActionsFor(result)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("탈퇴")
  @WithMockCustomUser(role = Role.ADMIN)
  void leave() throws Exception {
    MemberDeleteRequest deleteRequest = MemberTestHelper.createDeleteRequest(member.getId());
    String jsonValue = objectMapper.writeValueAsString(deleteRequest);

    EntityExchangeResult<byte[]> result = webTestClient.method(HttpMethod.DELETE)
        .uri(UriConstants.Mapping.MEMBERS_EDIT)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(jsonValue))
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody().returnResult();

    MockMvcWebTestClient.resultActionsFor(result)
        .andDo(print())
        .andExpect(status().isOk());
  }
}