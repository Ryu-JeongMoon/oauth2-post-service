package com.support.oauth2postservice.service.dto.request;

import static com.support.oauth2postservice.domain.entity.QMember.member;
import static com.support.oauth2postservice.domain.entity.QPost.post;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.data.querydsl.QSort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.querydsl.core.types.Path;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.QueryDslUtils;
import com.support.oauth2postservice.util.SortUtils;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import com.support.oauth2postservice.util.constant.RegexpConstants;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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

  @Schema(description = "상태", allowableValues = { "ACTIVE", "INACTIVE" })
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

  public void setDefaultOptionsForUser() {
    this.status = Status.ACTIVE;
    this.openedAt = LocalDateTime.now();
  }

  @Override
  protected QSort getQSort() {
    String[] sorts = getSorts();
    String[] orders = getOrders();

    if (sorts.length == 0)
      return PageConstants.POST_SEARCH_DEFAULT_SORT;

    List<CustomSort> customSorts = SortUtils.getCustomSorts(sorts, orders)
      .stream()
      .peek(pair -> pair.setParent(SortingColumn.getParent(pair.getProperty())))
      .collect(Collectors.toList());

    return QueryDslUtils.getQSort(customSorts, SortingColumn.toColumns());
  }

  @Getter
  @RequiredArgsConstructor
  public enum SortingColumn {

    TITLE(post, "title"),
    STATUS(post, "status"),
    CONTENT(post, "content"),
    NICKNAME(member, "nickname"),
    OPENED_AT(post, "openedAt");

    private final Path<?> parent;
    private final String property;

    public static Path<?> getParent(String property) {
      return Arrays.stream(values())
        .filter(sortingColumn -> sortingColumn.property.equals(property))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("Invalid property: " + property))
        .getParent();
    }

    public static String[] toColumns() {
      return Arrays.stream(values())
        .map(SortingColumn::getProperty)
        .toArray(String[]::new);
    }
  }
}
