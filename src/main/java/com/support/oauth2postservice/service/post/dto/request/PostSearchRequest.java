package com.support.oauth2postservice.service.post.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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

  /**
   * QueryDSL 사용하여 검색 시 DB가 아닌 애플리케이션에서 사용하는 Column 이름으로 검색하게 된다<br/>
   * 즉 openedAt 을 기준으로 검색하기 위해서 openedAt 을 String 으로 인자로 넘겨야 하는데<br/>
   * DB에 저장하기 위해 사용하는 필드명에는 "opened_at"으로 지정되어 있기에<br/>
   * 검색을 위한 필드명을 전달하기 위해 "openedAt"으로 지정해 둔 상수를 사용해야 한다<br/><br/>
   * ColumnConstants.Name.OPENED_AT => "opened_at"<br/>
   * PageConstants.Column.OPENED_AT => "openedAt"
   */
  private Pageable pageable;

  @Builder
  @JsonCreator
  public PostSearchRequest(String nickname, String title, String content, Status status, LocalDateTime openedAt,
                           int page, int size, List<Pair<String, Sort.Direction>> sorts) {
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.status = status;
    this.openedAt = openedAt;
    this.pageable = toPageable(page, size, sorts);
  }

  private Pageable toPageable(int page, int size, List<Pair<String, Sort.Direction>> sorts) {
    if (size == 0)
      size = PageConstants.DEFAULT_PAGE_SIZE;

    Sort sort = PageConstants.POST_SEARCH_DEFAULT_SORT;
    if (sorts != null) {
      sort = sorts.stream()
          .filter(Objects::nonNull)
          .map(pair -> new Sort.Order(pair.getRight(), pair.getLeft()))
          .collect(collectingAndThen(toList(), Sort::by));
    }

    return PageRequest.of(page, size, sort);
  }
}
