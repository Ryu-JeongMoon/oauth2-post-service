package com.support.oauth2postservice.domain.member.repository;

import com.support.oauth2postservice.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TestMemberRepository {

  private final EntityManager em;

  public void findOneMember() {
    String memberId = "0";
    em.createQuery("select m from Member m where m.id = :memberId", Member.class)
        .setParameter("memberId", memberId)
        .getSingleResult();
  }
}
