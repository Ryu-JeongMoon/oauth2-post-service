package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.domain.post.repository.PostRepository;
import com.support.oauth2postservice.security.service.CustomUserDetailsService;
import com.support.oauth2postservice.service.member.MemberService;
import com.support.oauth2postservice.service.post.PostService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith({MockitoExtension.class})
public class ServiceTest {

    protected static String USER_ID = "1";
    protected static String MANAGER_ID = "2";
    protected static String ADMIN_ID = "3";

    @Spy
    protected PasswordEncoder passwordEncoder;

    @Mock
    protected PostRepository postRepository;

    @Mock
    protected MemberRepository memberRepository;

    @InjectMocks
    protected PostService postService;

    @InjectMocks
    protected MemberService memberService;

    @InjectMocks
    protected CustomUserDetailsService customUserDetailsService;

}
