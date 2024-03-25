package com.andrezzb.coursearchive.program.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.program.models.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>{
  
}
