package com.andrezzb.coursearchive.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.course.models.CourseYear;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

@Repository
public interface CourseYearRepository
    extends JpaRepository<CourseYear, Long>, FilterFieldSpecification<CourseYear> {

  default Page<CourseYear> findAllByFilterFieldAndValue(Pageable pageable,
      String filterField, Object filterValue, Long couresId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (couresId != null) {
      baseSpec = baseSpec.and(filterByCourseId(couresId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<CourseYear> filterByCourseId(Long courseId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("course").get("id"),
        courseId);
  }
}
