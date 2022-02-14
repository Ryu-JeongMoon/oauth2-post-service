package com.support.oauth2postservice.helper;

import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.member.entity.Member;
import com.support.oauth2postservice.domain.post.entity.Post;
import com.support.oauth2postservice.service.post.dto.request.PostCreateRequest;
import com.support.oauth2postservice.service.post.dto.request.PostEditRequest;
import com.support.oauth2postservice.service.post.dto.request.PostSearchRequest;
import com.support.oauth2postservice.service.post.dto.response.PostReadResponse;

import java.time.LocalDateTime;

public class PostTestHelper {

    public static final String ID = "1234567890";
    public static final String TITLE = "panda";
    public static final String CONTENT = "bear";
    public static final LocalDateTime OPENED_AT = LocalDateTime.MIN;
    public static final LocalDateTime CLOSED_AT = LocalDateTime.MAX;

    public static Post getDefault(Member member) {
        return Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .openedAt(OPENED_AT)
                .closedAt(CLOSED_AT)
                .build();
    }

    public static PostCreateRequest getCreateRequest(String nickname) {
        return PostCreateRequest.builder()
                .nickname(nickname)
                .title(TITLE)
                .content(CONTENT)
                .openedAt(OPENED_AT)
                .closedAt(CLOSED_AT)
                .build();
    }

    public static PostEditRequest getEditRequest(Status status) {
        return PostEditRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .status(status)
                .openedAt(OPENED_AT)
                .closedAt(CLOSED_AT)
                .build();
    }

    public static PostSearchRequest getSearchRequest(String nickname) {
        return PostSearchRequest.builder()
                .nickname(nickname)
                .build();
    }

    public static PostReadResponse getReadResponse(Post post) {
        return PostReadResponse.builder()
                .id(ID)
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .openedAt(post.getOpenedAt())
                .build();
    }
}
