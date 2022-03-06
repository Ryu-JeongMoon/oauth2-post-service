package com.support.oauth2postservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

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

  @Size(max = ColumnConstants.Length.SEARCH)
  private Status status;

  @PastOrPresent
  @Size(max = ColumnConstants.Length.SEARCH)
  private LocalDateTime openedAt;

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
}
