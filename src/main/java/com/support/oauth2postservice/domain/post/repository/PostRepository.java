package com.support.oauth2postservice.domain.post.repository;

import com.support.oauth2postservice.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p where p.id = :id and p.status = 'ACTIVE'")
    Optional<Post> findActive(Long id);
}