package com.support.oauth2postservice.service;

import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import com.support.oauth2postservice.service.member.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith({MockitoExtension.class})
public class ServiceTest {

    protected static Long USER_ID = 1L;
    protected static Long MANAGER_ID = 2L;
    protected static Long ADMIN_ID = 3L;

    @InjectMocks
    protected MemberService memberService;

    @Spy
    protected PasswordEncoder passwordEncoder;

    @Mock
    protected MemberRepository memberRepository;
}
