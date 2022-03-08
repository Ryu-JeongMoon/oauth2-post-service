package com.support.oauth2postservice.domain.repository;

import com.support.oauth2postservice.domain.entity.Member;
import com.support.oauth2postservice.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  @Query("select r from RefreshToken r join fetch r.member where r.member.email = :email")
  Optional<RefreshToken> findByEmail(String email);

  @Query("select r from RefreshToken r join fetch r.member where r.member = :member")
  Optional<RefreshToken> findByMember(Member member);

  @Query("select r from RefreshToken r join fetch r.member where r.tokenValue = :tokenValue")
  Optional<RefreshToken> findByTokenValue(String tokenValue);
}
