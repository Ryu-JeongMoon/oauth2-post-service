package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.id = :id and m.status = 'ACTIVE'")
    Optional<Member> findActiveMember(Long id);
}
