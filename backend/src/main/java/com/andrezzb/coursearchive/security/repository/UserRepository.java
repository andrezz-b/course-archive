package com.andrezzb.coursearchive.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.andrezzb.coursearchive.security.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}
