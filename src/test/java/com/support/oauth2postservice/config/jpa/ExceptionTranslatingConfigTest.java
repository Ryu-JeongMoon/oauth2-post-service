package com.support.oauth2postservice.config.jpa;

import com.support.oauth2postservice.domain.member.repository.TestMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExceptionTranslatingConfigTest {

  @Autowired
  TestMemberRepository testMemberRepository;

  @Test
  @DisplayName("NoResultException -> EmptyResultDataAccessException")
  void translateException() {
    assertThrows(EmptyResultDataAccessException.class, () -> testMemberRepository.findOneMember());
  }
}