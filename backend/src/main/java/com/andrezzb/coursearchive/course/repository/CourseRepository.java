package com.andrezzb.coursearchive.course.repository;

import com.andrezzb.coursearchive.security.models.UserEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.repository.FilterFieldSpecification;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository
  extends JpaRepository<Course, Long>, FilterFieldSpecification<Course> {

  default Page<Course> findAllByFilterFieldAndValue(Pageable pageable, List<String> filterField,
    List<Object> filterValue, Long programId) {
    return findAllByFilterFieldAndValue(pageable, filterField, filterValue, programId, null);
  }

  default Page<Course> findAllByFilterFieldAndValue(Pageable pageable, List<String> filterField,
    List<Object> filterValue, Long programId, String username) {
    var baseSpec = filterByFieldAndValue(filterField, filterValue);
    if (programId != null) {
      baseSpec = baseSpec.and(filterByProgramId(programId));
    }
    if (username != null) {
      baseSpec = baseSpec.and(filterFavorites(username));
    }
    return findAll(baseSpec, pageable);
  }

  static Specification<Course> filterByProgramId(Long programId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("program").get("id"),
      programId);
  }

  static Specification<Course> filterFavorites(String username) {
    return (root, query, criteriaBuilder) -> {
      query.distinct(true);
      Subquery<UserEntity> userSubquery = query.subquery(UserEntity.class);
      Root<UserEntity> userRoot = userSubquery.from(UserEntity.class);
      Expression<Collection<Course>> userFavorites = userRoot.get("favoriteCourses");
      userSubquery.select(userRoot);
      userSubquery.where(criteriaBuilder.equal(userRoot.get("username"), username),
        criteriaBuilder.isMember(root, userFavorites));
      return criteriaBuilder.exists(userSubquery);
    };
  }
}
