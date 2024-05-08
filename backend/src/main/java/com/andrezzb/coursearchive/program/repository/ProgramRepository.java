package com.andrezzb.coursearchive.program.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.program.models.Program;

@Repository
public interface ProgramRepository
    extends JpaRepository<Program, Long>, JpaSpecificationExecutor<Program> {

  default Page<Program> findAllByFilterFieldAndValue(Pageable pageable,
      Program.FilterField filterField, Object filterValue, Long collegeId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (collegeId != null) {
      baseSpec = baseSpec.and(filterByCollegeId(collegeId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<Program> filterByFieldAndValue(Program.FilterField filterField,
      Object filterValue) {
    return (root, query, criteriaBuilder) -> {
      if (filterField == null || filterValue == null) {
        return null;
      }

      if (filterValue instanceof String) {
        return criteriaBuilder.like(root.get(filterField.toString()), "%" + filterValue + "%");
      }

      if (filterValue instanceof Number) {
        return criteriaBuilder.equal(root.get(filterField.toString()), filterValue);
      }

      return null;
    };

  }

  static Specification<Program> filterByCollegeId(Long collegeId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("college").get("id"),
        collegeId);
  }
}
