package com.support.oauth2postservice.learning;

import com.support.oauth2postservice.config.jpa.JpaAuditingConfig;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.PostRepository;
import com.support.oauth2postservice.helper.MemberTestHelper;
import com.support.oauth2postservice.helper.PostTestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Transactional
@SpringBootTest
@Import(JpaAuditingConfig.class)
public class InsertDataDataTest {

  @Autowired
  PostRepository postRepository;
  @Autowired
  MemberRepository memberRepository;

  @Disabled
  @Test
  @Rollback(value = false)
  @DisplayName("POST 데이터 추가")
  void insertPost() {
    Member admin = MemberTestHelper.createAdmin();
    memberRepository.save(admin);

    IntStream.range(0, 10000).parallel()
        .peek(i -> {
          try {
            Post post = PostTestHelper.getDefault(admin);
            Post source = Post.builder()
                .title(post.getTitle() + i)
                .content(post.getContent() + i)
                .build();

            post.changeFrom(source);
            postRepository.save(post);
          } catch (Exception e) {
            System.out.println("Error");
          }
        })
        .forEach(System.out::print);
  }
}
