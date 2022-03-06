package com.support.oauth2postservice.config;

import com.support.oauth2postservice.domain.enumeration.Role;
import com.support.oauth2postservice.service.dto.request.MemberSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

public class MemberSortArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String SORTS = "sorts";
  private static final String SIZE = "size";
  private static final String PAGE = "page";
  private static final String NICKNAME = "nickname";
  private static final String EMAIL = "email";
  private static final String ROLE = "role";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(MemberSearchRequest.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    List<Pair<String, Sort.Direction>> sortCondition = new ArrayList<>();

    String[] columnAndDirections = webRequest.getParameterMap().get(SORTS);

    if (columnAndDirections != null)
      for (String columnAndDirection : columnAndDirections) {
        String[] keyword = columnAndDirection.split(",");
        sortCondition.add(
            Pair.of(keyword[0],
                (keyword.length < 2 || keyword[1].equalsIgnoreCase(Sort.Direction.ASC.name())) ? Sort.Direction.ASC : Sort.Direction.DESC)
        );
      }

    return MemberSearchRequest.builder()
        .email(webRequest.getParameter(EMAIL))
        .nickname(webRequest.getParameter(NICKNAME))
        .role(toRole(webRequest.getParameter(ROLE)))
        .page(toInt(webRequest.getParameter(PAGE)))
        .size(toInt(webRequest.getParameter(SIZE)))
        .sorts(sortCondition)
        .build();
  }

  private Role toRole(String roleParam) {
    return StringUtils.isNotBlank(roleParam) ? Role.valueOfCaseInsensitively(roleParam) : null;
  }

  private Integer toInt(String param) {
    return param != null ? Integer.parseInt(param) : 0;
  }
}

