package com.support.oauth2postservice.util.constant;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.support.oauth2postservice.domain.entity.QMember;
import com.support.oauth2postservice.domain.entity.QPost;
import org.springframework.data.querydsl.QSort;

public class PageConstants {

  public static final int DEFAULT_PAGE_SIZE = 100;
  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final QSort POST_SEARCH_DEFAULT_SORT = QSort.by(new OrderSpecifier<>(Order.DESC, QPost.post.openedAt));
  public static final QSort MEMBER_SEARCH_DEFAULT_SORT = QSort.by(new OrderSpecifier<>(Order.DESC, QMember.member.createdAt));

  public static class Column {

    /**
     * QueryDSL 사용하여 검색 시 DB가 아닌 애플리케이션에서 사용하는 Column 이름으로 검색하게 된다<br/>
     * 즉 openedAt 을 기준으로 검색하기 위해서 `openedAt`을 String 타입의 인자로 넘겨야 하는데<br/>
     * DB에 저장하기 위해 사용하는 필드명에는 `opened_at`으로 지정되어 있기에<br/>
     * 검색을 위한 필드명을 전달하기 위해 `openedAt`으로 지정해 둔 상수를 사용해야 한다<br/><br/>
     * ColumnConstants.Name.OPENED_AT => `opened_at`<br/>
     * PageConstants.Column.OPENED_AT => `openedAt`
     */
    public static final String OPENED_AT = "openedAt";
    public static final String ROLE = "role";
  }
}
