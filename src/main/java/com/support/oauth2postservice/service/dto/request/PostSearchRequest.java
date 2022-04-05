package com.support.oauth2postservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.entity.QPost;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.QueryDslUtils;
import com.support.oauth2postservice.util.SortUtils;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import com.support.oauth2postservice.util.constant.RegexpConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchRequest extends PageAttributes {

  @Size(max = ColumnConstants.Length.SEARCH)
  @Schema(description = "닉네임", example = "panda")
  private String nickname;

  @Size(max = ColumnConstants.Length.SEARCH)
  @Schema(description = "제목", example = "oauth2-post-service")
  private String title;

  @Size(max = ColumnConstants.Length.SEARCH)
  @Schema(description = "내용", example = "oauth2-post-service")
  private String content;

  @Schema(description = "상태", allowableValues = {"ACTIVE", "INACTIVE"})
  private Status status;

  @PastOrPresent
  @Schema(description = "게시일", example = "2022-02-12 15:30", pattern = RegexpConstants.DATETIME)
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
    String[] sorts = getSorts();
    String[] orders = getOrders();

    if (sorts.length == 0)
      return PageConstants.POST_SEARCH_DEFAULT_SORT;

    List<Pair<String, Sort.Direction>> columnsAndDirections = SortUtils.getPairs(sorts, orders);

    String[] sortingColumns = Arrays.stream(SortingColumn.values())
        .map(SortingColumn::getColumnName)
        .toArray(String[]::new);

    return QueryDslUtils.getQSort(columnsAndDirections, QPost.post, sortingColumns);
  }

  public void setDefaultOptionsForUser() {
    this.status = Status.ACTIVE;
    this.openedAt = LocalDateTime.now();
  }

  @Getter
  @RequiredArgsConstructor
  private enum SortingColumn {
    TITLE("title"),
    STATUS("status"),
    CONTENT("content"),
    NICKNAME("member.nickname"),
    OPENED_AT("openedAt");

    private final String columnName;
  }
}
