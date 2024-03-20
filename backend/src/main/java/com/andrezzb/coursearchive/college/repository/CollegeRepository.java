package com.andrezzb.coursearchive.college.repository;

import com.andrezzb.coursearchive.college.models.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
}
