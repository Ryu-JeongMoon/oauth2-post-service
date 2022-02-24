package com.support.oauth2postservice.config;

import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SortArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String SORTS = "sorts";
  private static final String SIZE = "size";
  private static final String PAGE = "page";
  private static final String NICKNAME = "nickname";
  private static final String TITLE = "title";
  private static final String CONTENT = "content";
  private static final String STATUS = "status";
  private static final String OPENED_AT = "openedAt";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(PostSearchRequest.class);
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

    LocalDateTime openedAt = getOpenedAt(webRequest.getParameter(OPENED_AT));
    Status status = getStatus(webRequest.getParameter(STATUS));

    return PostSearchRequest.builder()
        .title(webRequest.getParameter(TITLE))
        .content(webRequest.getParameter(CONTENT))
        .nickname(webRequest.getParameter(NICKNAME))
        .status(status)
        .page(getIntValue(webRequest.getParameter(PAGE)))
        .size(getIntValue(webRequest.getParameter(SIZE)))
        .openedAt(openedAt)
        .sorts(sortCondition)
        .build();
  }

  private Status getStatus(String statusName) {
    return EnumUtils.getEnumIgnoreCase(Status.class, statusName);
  }

  private LocalDateTime getOpenedAt(String openedAtParam) {
    LocalDateTime openedAt = null;

    if (StringUtils.isNotBlank(openedAtParam))
      openedAt = LocalDateTime.parse(openedAtParam);

    return openedAt;
  }

  private Integer getIntValue(String param) {
    return param != null ? Integer.parseInt(param) : 0;
  }
}
