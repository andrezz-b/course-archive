package com.andrezzb.coursearchive.college.repository;

import com.andrezzb.coursearchive.college.models.College;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository
    extends JpaRepository<College, Long>, JpaSpecificationExecutor<College> {

  default Page<College> findAllByFilterFieldAndFilterValue(Pageable pageable,
      College.FilterField filterField, Object filterValue) {
    return findAll(
        (Root<College> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
          if (filterField == null || filterValue == null) {
            return null;
          }

          if (filterValue instanceof String) {
            return criteriaBuilder.like(root.get(filterField.toString()),
                "%" + filterValue + "%");
          }

          if (filterValue instanceof Number) {
            return criteriaBuilder.equal(root.get(filterField.toString()), filterValue);
          }

          return null;
        }, pageable);
  }
}
