package com.support.oauth2postservice.service.post.dto.request;

import com.support.oauth2postservice.service.dto.request.PostSearchRequest;
import com.support.oauth2postservice.util.constant.PageConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class PostSearchRequestTest {

  @Test
  @DisplayName("Default 페이지 설정 확인")
  void defaultPageableSetting() {
    PostSearchRequest postSearchRequest = PostSearchRequest.builder()
        .nickname("panda")
        .build();

    Pageable pageable = postSearchRequest.getPageable();

    Assertions.assertThat(pageable.isPaged()).isTrue();
    Assertions.assertThat(pageable.getPageSize()).isEqualTo(PageConstants.DEFAULT_PAGE_SIZE);
    Assertions.assertThat(pageable.getPageNumber()).isEqualTo(PageConstants.DEFAULT_PAGE_NUMBER);
  }

}