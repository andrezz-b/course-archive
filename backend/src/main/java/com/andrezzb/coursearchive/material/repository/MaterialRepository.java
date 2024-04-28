package com.andrezzb.coursearchive.material.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.material.models.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
  
}
