package com.andrezzb.coursearchive.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.security.models.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> findByEmail(String email);
}
