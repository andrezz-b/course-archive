package com.andrezzb.coursearchive.material.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.material.models.MaterialGroup;

@Repository
public interface MaterialGroupRepository extends JpaRepository<MaterialGroup, Long> {

  @Query("SELECT MAX(displayOrder) FROM MaterialGroup WHERE courseYear.id = :courseYearId")
  Optional<Short> findMaxOrder(@Param("courseYearId") Long courseYearId);

  @Modifying
  @Query("UPDATE MaterialGroup SET displayOrder = displayOrder + 1 WHERE courseYear.id = :courseYearId AND displayOrder >= :order")
  void incrementDisplayOrder(@Param("courseYearId") Long courseYearId, @Param("order") Short displayOrder);
}
