package com.andrezzb.coursearchive.course.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.dto.CourseCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseUpdateDto;
import com.andrezzb.coursearchive.course.exceptions.CourseNotFoundException;
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

  @PreAuthorize("hasRole('USER')")
  public Page<Course> findAllCoursesPaged(Pageable p) {
    return courseRepository.findAll(p);
  }

  @PreAuthorize("hasRole('USER')")
  public Course findCourseById(Long id) {
    return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#courseDto.programId, 'com.andrezzb.coursearchive.program.models.Program', create) || hasRole('MANAGER')")
  public Course createCourse(CourseCreateDto courseDto) {
    var program = programService.findProgramById(courseDto.getProgramId());
    Course course = modelMapper.map(courseDto, Course.class);
    course.setProgram(program);
    Course savedCourse = courseRepository.save(course);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedCourse, username, BasePermission.ADMINISTRATION);
    return savedCourse;
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.course.models.Course', write) || hasRole('MANAGER')")
  public Course updatCourse(Long id, CourseUpdateDto courseDto) {
    Course course = findCourseById(id);
    modelMapper.map(courseDto, course);
    return courseRepository.save(course);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.course.models.Course', delete) || hasRole('MANAGER')")
  public void deleteCourseById(Long id) {
    courseRepository.deleteById(id);
  }

}
