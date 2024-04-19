package com.andrezzb.coursearchive.course.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.repository.CourseRepository;
import com.andrezzb.coursearchive.program.services.ProgramService;
import com.andrezzb.coursearchive.security.services.AclUtilService;

@Service
public class CourseService {
  private final CourseRepository courseRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final ProgramService programService;

  public CourseService(CourseRepository courseRepository, AclUtilService aclUtilService,
      ModelMapper modelMapper,
      ProgramService programService) {
    this.courseRepository = courseRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.programService = programService;
  }

}
