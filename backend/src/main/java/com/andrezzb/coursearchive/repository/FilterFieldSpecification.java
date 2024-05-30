package com.andrezzb.coursearchive.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface FilterFieldSpecification<T> extends JpaSpecificationExecutor<T> {

  default Specification<T> filterByFieldAndValue(List<String> filterFields, List<Object> filterValues) {
    return (root, query, criteriaBuilder) -> {
      if (filterFields == null || filterValues == null) {
        return null;
      }

      if (filterFields.size() != filterValues.size()) {
        return null;
      }

      List<Predicate> predicates = new ArrayList<>();
      for (int i = 0; i < filterFields.size(); i++) {
        var filterField = filterFields.get(i);
        var filterValue = filterValues.get(i);

        if (filterField == null || filterValue == null) {
          continue;
        }

        var predicate = filterByFieldAndValue(filterField, filterValue).toPredicate(root, query, criteriaBuilder);
        if (predicate != null) {
          predicates.add(predicate);
        }
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

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
