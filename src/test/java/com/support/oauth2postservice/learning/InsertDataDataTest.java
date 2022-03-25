package com.support.oauth2postservice.learning;

import com.support.oauth2postservice.config.jpa.JpaAuditingConfig;
import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.Post;
import com.support.oauth2postservice.domain.enumeration.Status;
import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.PostRepository;
import com.support.oauth2postservice.helper.MemberTestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
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
    Member manger = MemberTestHelper.createManger();
    memberRepository.save(manger);

    IntStream.range(1, 20000).parallel()
        .peek(i -> {
          try {
            Post post = Post.builder()
                .member(manger)
                .title("OAuth2-Service" + i)
                .content("OAuth2-Support" + i)
                .status(Status.ACTIVE)
                .openedAt(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0).plusDays(i))
                .closedAt(LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0).plusDays(i))
                .build();

            postRepository.save(post);
          } catch (Exception e) {
            System.out.println("Error");
          }
        })
        .forEach(System.out::print);
  }
}
