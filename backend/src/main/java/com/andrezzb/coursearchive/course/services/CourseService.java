package com.andrezzb.coursearchive.course.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.dto.CourseCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseDto;
import com.andrezzb.coursearchive.course.dto.CourseUpdateDto;
import com.andrezzb.coursearchive.course.exceptions.CourseNotFoundException;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.repository.CourseRepository;
import com.andrezzb.coursearchive.program.services.ProgramService;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

import java.util.List;

@Service
public class CourseService {
  private final CourseRepository courseRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final ProgramService programService;

  public CourseService(CourseRepository courseRepository, AclUtilService aclUtilService,
    ModelMapper modelMapper, ProgramService programService) {
    this.courseRepository = courseRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.programService = programService;
  }

  @PreAuthorize("hasRole('USER')")
  public Page<CourseDto> findAllCoursesPaged(Pageable p, List<String> filterField,
    List<Object> filterValue, Long programId) {
    Page<Course> courses =
      courseRepository.findAllByFilterFieldAndValue(p, filterField, filterValue, programId);
    return courses.map(course -> modelMapper.map(course, CourseDto.class));
  }

  @PreAuthorize("hasRole('USER')")
  public Page<CourseDto> findAllFavoritesPaged(Pageable p, List<String> filterField,
    List<Object> filterValue, String username) {
    Page<Course> courses = courseRepository.findAllByFilterFieldAndValue(p, filterField, filterValue, null, username);
    return courses.map(course -> modelMapper.map(course, CourseDto.class));
  }

  @PreAuthorize("hasRole('USER')")
  public Course findCourseById(Long id) {
    return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
  }

  public CourseDto mapCourseToDto(Course course) {
    return modelMapper.map(course, CourseDto.class);
  }

  @Transactional
  @PreAuthorize(
    "hasPermission(#courseDto.programId, 'com.andrezzb.coursearchive.program.models.Program', 'create') || hasRole('MANAGER')")
  public CourseDto createCourse(CourseCreateDto courseDto) {
    var program = programService.findProgram(courseDto.getProgramId());
    Course course = modelMapper.map(courseDto, Course.class);
    course.setProgram(program);
    course.setId(null);
    Course savedCourse = courseRepository.save(course);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedCourse, username, AclPermission.ADMINISTRATION);
    return modelMapper.map(savedCourse, CourseDto.class);
  }

  @PreAuthorize(
    "hasPermission(#id, 'com.andrezzb.coursearchive.course.models.Course', 'write') || hasRole('MANAGER')")
  public CourseDto updatCourse(Long id, CourseUpdateDto courseDto) {
    Course course = findCourseById(id);
    modelMapper.map(courseDto, course);
    var savedCourse = courseRepository.save(course);
    return modelMapper.map(savedCourse, CourseDto.class);
  }

  @PreAuthorize(
    "hasPermission(#id, 'com.andrezzb.coursearchive.course.models.Course', 'delete') || hasRole('MANAGER')")
  public void deleteCourseById(Long id) {
    courseRepository.deleteById(id);
  }

}
