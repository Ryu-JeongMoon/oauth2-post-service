package com.support.oauth2postservice.config;

import com.support.oauth2postservice.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class JpaTest {

    @Autowired
    protected MemberRepository memberRepository;
}
