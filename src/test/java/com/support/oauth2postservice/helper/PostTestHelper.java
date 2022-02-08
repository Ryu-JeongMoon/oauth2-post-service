package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;

import java.time.LocalDateTime;

public class PostTestHelper {

    public static final String TITLE = "panda";
    public static final String CONTENT = "bear";
    public static final LocalDateTime OPENED_AT = LocalDateTime.MIN;
    public static final LocalDateTime CLOSED_AT = LocalDateTime.MAX;

    public static Post createDefault(Member member) {
        return Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .openedAt(OPENED_AT)
                .closedAt(CLOSED_AT)
                .build();
    }
}
