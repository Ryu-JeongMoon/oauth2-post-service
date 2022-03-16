package com.support.oauth2postservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.entity.QMember;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.util.EnumUtils;
import com.support.oauth2postservice.util.QueryDslUtils;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import com.support.oauth2postservice.util.constant.PageConstants;
import lombok.*;
import org.springframework.data.querydsl.QSort;

import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchRequest extends PageAttributes {

  @Size(max = ColumnConstants.Length.SEARCH)
  private String email;

  @Size(max = ColumnConstants.Length.SEARCH)
  private String nickname;

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
    if (getSorts().isEmpty())
      return PageConstants.MEMBER_SEARCH_DEFAULT_SORT;

    String[] keywords = EnumUtils.toStringArray(SortingColumn.values());
    return QueryDslUtils.getQSort(getSorts(), QMember.member, keywords);
  }

  private enum SortingColumn {
    EMAIL,
    ROLE,
    NICKNAME
  }
}