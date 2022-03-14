package com.support.oauth2postservice.service.dto.request;

import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDeleteRequest {

  @NotBlank
  @Size(max = ColumnConstants.Length.ID)
  private String id;

  @Size(max = ColumnConstants.Length.DEFAULT_MAX)
  private String password;

  @Builder
  public MemberDeleteRequest(String id, String password) {
    this.id = id;
    this.password = password;
  }
}
