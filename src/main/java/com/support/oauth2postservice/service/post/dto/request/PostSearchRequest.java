package com.support.oauth2postservice.service.post.dto.request;

import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchRequest {

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
  private Pageable pageable = PageRequest.of(
      PageConstants.DEFAULT_PAGE_NUMBER,
      PageConstants.DEFAULT_PAGE_SIZE,
      Sort.by(PageConstants.DEFAULT_SORT_DIRECTION, PageConstants.Column.OPENED_AT)
  );

  @Builder
  public PostSearchRequest(String nickname, String title, String content, Status status, LocalDateTime openedAt, Pageable pageable) {
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.status = status;
    this.openedAt = openedAt;

    if (pageable != null)
      this.pageable = setPageable(pageable);
  }

  private Pageable setPageable(Pageable pageable) {
    Sort sort = pageable.getSort().isEmpty() ? this.pageable.getSort() : pageable.getSort();
    int pageSize = pageable.getPageSize() == 0 ? this.pageable.getPageSize() : pageable.getPageSize();
    int pageNumber = pageable.getPageNumber() == 0 ? this.pageable.getPageNumber() : pageable.getPageNumber();

    return PageRequest.of(pageNumber, pageSize, sort);
  }
}
