package com.support.oauth2postservice.util;

import com.querydsl.jpa.JPQLQuery;
import com.support.oauth2postservice.config.JpaTest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;
import com.support.oauth2postservice.service.post.dto.response.QPostReadResponse;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static com.support.oauth2postservice.domain.post.entity.QPost.post;


class PagingHelperTest extends JpaTest {

  @Autowired
  protected PagingHelper pagingHelper;

  @Test
  @DisplayName("Paging 간소화")
  void getPage() {
    PageRequest pageRequest = PageRequest.of(50, 100, Sort.by(Sort.Direction.ASC, ColumnConstants.Name.TITLE));

    JPQLQuery<PostReadResponse> query = queryFactory
        .select(new QPostReadResponse(post.id, post.member.nickname, post.title, post.content, post.openedAt))
        .from(post)
        .join(post.member);

    Page<PostReadResponse> page = pagingHelper.getPage(post.getType(), query, pageRequest);

    Assertions.assertThat(page).isNotNull();
  }
}