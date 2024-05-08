package com.andrezzb.coursearchive.college.repository;

import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository
    extends JpaRepository<College, Long>, FilterFieldSpecification<College> {

  default Page<College> findAllByFilterFieldAndFilterValue(Pageable pageable,
      String filterField, Object filterValue) {
    return findAll(filterByFieldAndValue(filterField, filterValue), pageable);
  }
}
