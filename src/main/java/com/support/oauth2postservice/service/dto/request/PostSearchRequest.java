package com.support.oauth2postservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.entity.QPost;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.QueryDslUtils;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.*;
import org.springframework.data.querydsl.QSort;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchRequest extends PageAttributes {

  @Size(max = ColumnConstants.Length.SEARCH)
  private String nickname;

  @Size(max = ColumnConstants.Length.SEARCH)
  private String title;

  @Size(max = ColumnConstants.Length.SEARCH)
  private String content;

  private Status status;

  @PastOrPresent
  private LocalDateTime openedAt;

  @Builder
  @JsonCreator
  public PostSearchRequest(String nickname, String title, String content, Status status, LocalDateTime openedAt) {
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.status = status;
    this.openedAt = openedAt;
  }

  @Override
  protected QSort getQSort() {
    if (getSorts().isEmpty())
      return PageConstants.POST_SEARCH_DEFAULT_SORT;

    String[] keywords = Arrays.stream(SortingColumn.values())
        .map(SortingColumn::getColumnName)
        .toArray(String[]::new);

    return QueryDslUtils.getQSort(getSorts(), QPost.post, keywords);
  }

  @Getter
  @RequiredArgsConstructor
  private enum SortingColumn {
    TITLE("title"),
    STATUS("status"),
    CONTENT("content"),
    NICKNAME("nickname"),
    OPENED_AT("openedAt");

    private final String columnName;
  }
}
