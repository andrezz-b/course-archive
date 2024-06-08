package com.andrezzb.coursearchive.material.repository;

import com.andrezzb.coursearchive.material.models.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAllByCourseYearId(Long courseYearId, Sort sort);

  @Query("SELECT COUNT(m) FROM Material m JOIN m.tags t WHERE t.id = :tagId")
  Long countMaterialsByTagId(Long tagId);
}
