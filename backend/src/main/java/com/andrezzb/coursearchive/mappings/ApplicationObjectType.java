package com.andrezzb.coursearchive.mappings;

import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.college.repository.CollegeRepository;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.models.CourseYear;
import com.andrezzb.coursearchive.course.repository.CourseRepository;
import com.andrezzb.coursearchive.course.repository.CourseYearRepository;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
import com.andrezzb.coursearchive.material.repository.MaterialGroupRepository;
import com.andrezzb.coursearchive.material.repository.MaterialRepository;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.repository.ProgramRepository;
import com.andrezzb.coursearchive.security.models.AclSecured;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public enum ApplicationObjectType {
  COLLEGE(College.class, CollegeRepository.class),
  PROGRAM(Program.class, ProgramRepository.class),
  COURSE(Course.class, CourseRepository.class),
  COURSE_YEAR(CourseYear.class, CourseYearRepository.class),
  MATERIAL_GROUP(MaterialGroup.class, MaterialGroupRepository.class),
  MATERIAL(Material.class, MaterialRepository.class);

  private final Class<? extends AclSecured> objectClass;
  private final Class<? extends JpaRepository<? extends AclSecured, Long>> repositoryClass;

  ApplicationObjectType(Class<? extends AclSecured> objectClass, Class<? extends JpaRepository<? extends AclSecured, Long>> repositoryClass) {
    this.objectClass = objectClass;
    this.repositoryClass = repositoryClass;
  }

  public static ApplicationObjectType fromString(String objectType) {
    return ApplicationObjectType.valueOf(objectType.toUpperCase().replace("-", "_"));
  }
}
