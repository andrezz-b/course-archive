package com.andrezzb.coursearchive.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.andrezzb.coursearchive.course.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  
}
