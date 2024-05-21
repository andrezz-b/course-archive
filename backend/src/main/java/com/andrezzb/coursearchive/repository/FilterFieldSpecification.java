package com.andrezzb.coursearchive.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilterFieldSpecification<T> extends JpaSpecificationExecutor<T> {

  default Specification<T> filterByFieldAndValue(String filterField,
                                                 Object filterValue) {
    return (root, query, criteriaBuilder) -> {
      if (filterField == null || filterValue == null) {
        return null;
      }

      if (filterValue instanceof String) {
        return criteriaBuilder.like(
            criteriaBuilder.lower(root.get(filterField)),
            "%" + ((String) filterValue).toLowerCase() + "%");
      }

      if (filterValue instanceof Number) {
        return criteriaBuilder.equal(root.get(filterField), filterValue);
      }

      return null;
    };

  }
}
