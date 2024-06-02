package com.andrezzb.coursearchive.material.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

import java.util.List;

@Repository
public interface MaterialRepository
  extends JpaRepository<Material, Long>, FilterFieldSpecification<Material> {


  default Page<Material> findAllByFilterFieldAndValue(Pageable pageable, List<String> filterField,
    List<Object> filterValue, Long materialGroupId, Long courseYearId) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (materialGroupId != null) {
      baseSpec = baseSpec.and(filterByMaterialGroupId(materialGroupId));
    }
    if (courseYearId != null) {
      baseSpec = baseSpec.and(filterByCourseYearId(courseYearId));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<Material> filterByMaterialGroupId(Long materialGroupId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
      root.get("materialGroup").get("id"), materialGroupId);
  }

  static Specification<Material> filterByCourseYearId(Long courseYearId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
      root.get("materialGroup").get("courseYear").get("id"), courseYearId);
  }

}
