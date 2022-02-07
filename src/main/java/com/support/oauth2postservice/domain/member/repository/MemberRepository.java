package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
