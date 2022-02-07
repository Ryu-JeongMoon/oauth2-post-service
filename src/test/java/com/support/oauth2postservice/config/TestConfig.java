package com.support.oauth2postservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EnableJpaAuditing
@TestConfiguration
public class TestConfig {

    @PersistenceContext
    EntityManager entityManager;

}
