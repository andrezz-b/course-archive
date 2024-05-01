package com.andrezzb.coursearchive.file.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.material.models.Material;

@Repository
public interface FileRepository extends JpaRepository<MaterialFile, Long>{

  Optional<MaterialFile> findByMaterial(Material material);
  Optional<MaterialFile> findByMaterialId(Long materialId);
  
  
}
