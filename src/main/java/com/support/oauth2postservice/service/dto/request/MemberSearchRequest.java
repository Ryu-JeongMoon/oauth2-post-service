package com.support.oauth2postservice.service.dto.request;

import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.querydsl.QSort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.util.EnumUtils;
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
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchRequest extends PageAttributes {

  @Size(max = ColumnConstants.Length.SEARCH)
  @Schema(description = "이메일", example = "panda@gmail.com", pattern = RegexpConstants.EMAIL)
  private String email;

  @Size(max = ColumnConstants.Length.SEARCH)
  @Schema(description = "닉네임", example = "panda", maxLength = 20)
  private String nickname;

  @Schema(description = "권한", allowableValues = { "USER", "MANAGER", "ADMIN" }, maxLength = 7)
  private Role role;

  @Builder
  @JsonCreator
  public MemberSearchRequest(String email, String nickname, Role role) {
    this.email = email;
    this.nickname = nickname;
    this.role = role;
  }

  @Override
  public QSort getQSort() {
    String[] sorts = getSorts();
    String[] orders = getOrders();

    if (sorts.length == 0)
      return PageConstants.MEMBER_SEARCH_DEFAULT_SORT;

    List<CustomSort> columnsAndDirections = SortUtils.getCustomSorts(sorts, orders);

    String[] sortingColumns = EnumUtils.toStringArray(SortingColumn.values());
    return QueryDslUtils.getQSort(columnsAndDirections, sortingColumns);
  }

  private enum SortingColumn {
    EMAIL,
    ROLE,
    NICKNAME
  }
}
