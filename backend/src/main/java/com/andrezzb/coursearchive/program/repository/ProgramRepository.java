package com.andrezzb.coursearchive.program.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

import java.util.List;

@Repository
public interface ProgramRepository
  extends JpaRepository<Program, Long>, FilterFieldSpecification<Program> {

  default Page<Program> findAllByFilterFieldAndValue(Pageable pageable,
                                                     List<String> filterField, List<Object> filterValue, Long collegeId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (collegeId != null) {
      baseSpec = baseSpec.and(filterByCollegeId(collegeId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<Program> filterByCollegeId(Long collegeId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("college").get("id"),
      collegeId);
  }
}
