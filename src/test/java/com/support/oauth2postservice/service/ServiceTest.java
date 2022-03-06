package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.repository.MemberRepository;
import com.support.oauth2postservice.domain.repository.PostRepository;
import com.support.oauth2postservice.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith({MockitoExtension.class})
public abstract class ServiceTest {

  protected static String USER_ID = "1";
  protected static String MANAGER_ID = "2";
  protected static String ADMIN_ID = "3";

  protected PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  @Mock
  protected PostRepository postRepository;
  @InjectMocks
  protected PostService postService;

  @Mock
  protected MemberRepository memberRepository;
  @InjectMocks
  protected MemberService memberService;
  @InjectMocks
  protected CustomUserDetailsService customUserDetailsService;
}
