package com.andrezzb.coursearchive.security.repository;

import java.util.List;
import java.util.Optional;

import com.andrezzb.coursearchive.repository.FilterFieldSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.security.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, FilterFieldSpecification<UserEntity> {
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  boolean existsByUsername(String username);

  default Page<UserEntity> findAllByFilterFieldAndFilterValue(Pageable pageable,
                                                              List<String> filterField, List<Object> filterValue) {
    return findAll(filterByFieldAndValue(filterField, filterValue), pageable);
  }
}
