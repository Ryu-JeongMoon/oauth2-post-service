package com.support.oauth2postservice.config;

import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.domain.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@DataJpaTest
@Import(TestConfig.class)
public class JpaTest {

  protected static String USER_ID;
  protected static String MANAGER_ID;
  protected static String ADMIN_ID;

  protected static String POST_ID;
  protected static String POST_TITLE;

  @Autowired
  protected EntityManager entityManager;

  @Autowired
  protected PostRepository postRepository;

  @Autowired
  protected MemberRepository memberRepository;

}
