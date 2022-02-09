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
        em.createQuery("select m from Member m where m.id = 0", Member.class).getSingleResult();
    }
}
