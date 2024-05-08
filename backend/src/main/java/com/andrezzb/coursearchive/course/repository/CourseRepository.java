package com.andrezzb.coursearchive.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

@Repository
public interface CourseRepository
    extends JpaRepository<Course, Long>, FilterFieldSpecification<Course> {


  default Page<Course> findAllByFilterFieldAndValue(Pageable pageable,
      String filterField, Object filterValue, Long programId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (programId != null) {
      baseSpec = baseSpec.and(filterByProgramId(programId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<Course> filterByProgramId(Long programId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("program").get("id"),
        programId);
  }
}
