package com.support.oauth2postservice.controller.view;

import com.support.oauth2postservice.controller.AbstractWebMvcTest;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import com.support.oauth2postservice.helper.WithMockCustomUser;
import com.support.oauth2postservice.security.config.JwtSecurityConfig;
import com.support.oauth2postservice.security.config.SecurityConfig;
import com.support.oauth2postservice.security.jwt.LocalTokenAuthenticationFilter;
import com.support.oauth2postservice.security.jwt.OAuth2TokenAuthenticationFilter;
import com.support.oauth2postservice.service.PostService;
import com.support.oauth2postservice.service.dto.response.PostReadResponse;
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
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
    controllers = PostViewController.class,
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
class PostViewControllerTest extends AbstractWebMvcTest {

  @MockBean
  PostService postService;

  PostReadResponse postReadResponse;

  @BeforeEach
  void setUp() {
    Member member = MemberTestHelper.createUser();
    Post post = PostTestHelper.getDefault(member);
    postReadResponse = PostTestHelper.getReadResponse(post);
  }

  @Nested
  @DisplayName("게시글 목록 조회")
  class GetPostsTest {

    @Test
    @DisplayName("검색 조건 없는 조회")
    @WithMockCustomUser(role = Role.ADMIN)
    void getPosts() throws Exception {
      PageImpl<PostReadResponse> readResponses = new PageImpl<>(Collections.singletonList(postReadResponse));
      Mockito.when(postService.searchByCondition(any(), any())).thenReturn(readResponses);

      mockMvc.perform(
              get(UriConstants.Mapping.POSTS)
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              view().name("post/list")
          );
    }

    @Test
    @DisplayName("검색 조건 있는 조회")
    @WithMockCustomUser(role = Role.ADMIN)
    void getPosts_successBySearchRequest() throws Exception {
      PageImpl<PostReadResponse> readResponses = new PageImpl<>(Collections.singletonList(postReadResponse));
      Mockito.when(postService.searchByCondition(any(), any())).thenReturn(readResponses);

      mockMvc.perform(
              get(UriConstants.Mapping.POSTS)
                  .queryParam("content", "panda")
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              view().name("post/list")
          );
    }
  }

  @Nested
  @DisplayName("게시글 상세 조회")
  class GetPostTest {

    @Test
    @DisplayName("성공")
    void getPost() throws Exception {
      when(postService.findById(any())).thenReturn(postReadResponse);

      mockMvc.perform(
              get(UriConstants.Mapping.POSTS_DETAIL.replace("{id}", "1"))
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              view().name("post/detail")
          );
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 게시글")
    void getPost_failByNotExistsId() {
      assertThrows(NestedServletException.class, () ->
          mockMvc.perform(
                  get(UriConstants.Mapping.POSTS_DETAIL.replace("{id}", "1"))
              )
              .andDo(print())
              .andExpect(view().name("post/detail")));
    }
  }

  @Nested
  @DisplayName("수정 페이지")
  class EditPageTest {

    @Test
    @DisplayName("성공")
    void editPage() throws Exception {
      when(postService.findById(any())).thenReturn(postReadResponse);

      mockMvc.perform(
              get(UriConstants.Mapping.POSTS_EDIT.replace("{id}", "1"))
          )
          .andDo(print())
          .andExpectAll(
              status().isOk(),
              view().name("post/edit")
          );
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 게시글")
    void editPage_failByNotExistsId() throws Exception {
      when(postService.findById(eq("1"))).thenThrow(IllegalArgumentException.class);

      mockMvc.perform(
              get(UriConstants.Mapping.POSTS_EDIT.replace("{id}", "1"))
          )
          .andDo(print())
          .andExpect(status().isBadRequest());
    }
  }
}