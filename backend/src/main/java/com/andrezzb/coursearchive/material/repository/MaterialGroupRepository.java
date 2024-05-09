package com.andrezzb.coursearchive.material.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

@Repository
public interface MaterialGroupRepository
    extends JpaRepository<MaterialGroup, Long>, FilterFieldSpecification<MaterialGroup> {

  @Query("SELECT MAX(displayOrder) FROM MaterialGroup WHERE courseYear.id = :courseYearId")
  Optional<Short> findMaxOrder(@Param("courseYearId") Long courseYearId);

  @Query("SELECT COUNT(*) FROM MaterialGroup WHERE courseYear.id = :courseYearId")
  Optional<Long> countByCourseYearId(@Param("courseYearId") Long courseYearId);

  @Modifying
  @Query("UPDATE MaterialGroup SET displayOrder = displayOrder + 1 WHERE courseYear.id = :courseYearId AND displayOrder >= :order")
  void incrementDisplayOrder(@Param("courseYearId") Long courseYearId,
      @Param("order") Short displayOrder);

  default Page<MaterialGroup> findAllByFilterFiledAndValue(Pageable pageable, String filterField,
      Object filterValue, Long courseYearId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (courseYearId != null) {
      baseSpec = baseSpec.and(filterByCourseYearId(courseYearId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<MaterialGroup> filterByCourseYearId(Long courseYearId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("courseYear").get("id"),
        courseYearId);
  }
}
