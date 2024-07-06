package com.andrezzb.coursearchive.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Slf4j
public class DataInitializer {

  @Bean
  public CommandLineRunner initializeRoles(JdbcTemplate jdbcTemplate) {
    return args -> {

      jdbcTemplate.update("INSERT INTO role (id, name) SELECT 1, 'USER' WHERE NOT EXISTS (SELECT 1 FROM role WHERE id=1)");

      jdbcTemplate.update("INSERT INTO role (id, name) SELECT 2, 'MANAGER' WHERE NOT EXISTS (SELECT 1 FROM role WHERE id=2)");

      jdbcTemplate.update("INSERT INTO role (id, name) SELECT 3, 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM role WHERE id=3)");
    };
  }
}

