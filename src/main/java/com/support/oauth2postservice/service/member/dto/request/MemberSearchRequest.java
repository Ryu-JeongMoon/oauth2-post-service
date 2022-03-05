package com.support.oauth2postservice.service.member.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.support.oauth2postservice.domain.PageAttributes;
import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchRequest extends PageAttributes {

  @Size(max = ColumnConstants.Length.SEARCH)
  private String email;

  @Size(max = ColumnConstants.Length.SEARCH)
  private String nickname;

  @Size(max = ColumnConstants.Length.SEARCH)
  private Role role;

  private Pageable pageable;

  @Builder
  @JsonCreator
  public MemberSearchRequest(String email, String nickname, Role role,
                             int page, int size, List<Pair<String, Sort.Direction>> sorts) {
    this.email = email;
    this.nickname = nickname;
    this.role = role;
    this.pageable = toPageable(page, size, sorts);
  }
}
