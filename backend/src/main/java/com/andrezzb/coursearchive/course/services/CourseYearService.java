package com.andrezzb.coursearchive.course.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.dto.CouresYearDto;
import com.andrezzb.coursearchive.course.dto.CourseYearCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseYearUpdateDto;
import com.andrezzb.coursearchive.course.exceptions.CourseYearNotFoundException;
import com.andrezzb.coursearchive.course.models.CourseYear;
import com.andrezzb.coursearchive.course.repository.CourseYearRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

@Service
public class CourseYearService {

  private final CourseYearRepository courseYearRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final CourseService courseService;


  public CourseYearService(CourseYearRepository courseYearRepository, AclUtilService aclUtilService,
      ModelMapper modelMapper,
      CourseService courseService) {
    this.courseYearRepository = courseYearRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.courseService = courseService;
  }

  @PreAuthorize("hasRole('USER')")
  public Page<CouresYearDto> findAllCourseYearsPaged(Pageable p) {
    return findAllCourseYearsPaged(p, null, null, null);
  }

  @PreAuthorize("hasRole('USER')")
  public Page<CouresYearDto> findAllCourseYearsPaged(Pageable p, String filterField,
      Object filterValue, Long courseId) {
    var years =
        courseYearRepository.findAllByFilterFieldAndValue(p, filterField, filterValue, courseId);
    return years.map(year -> modelMapper.map(year, CouresYearDto.class));
  }

  @PreAuthorize("hasRole('USER')")
  public CouresYearDto findCourseYearById(Long id) {
    return modelMapper.map(findCourseYear(id), CouresYearDto.class);
  }

  public CourseYear findCourseYear(Long id) {
    return courseYearRepository.findById(id).orElseThrow(() -> new CourseYearNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#courseYearDto.courseId, 'com.andrezzb.coursearchive.course.models.Course', create) || hasRole('MANAGER')")
  public CouresYearDto createCourseYear(CourseYearCreateDto courseYearDto) {
    var course = courseService.findCourseById(courseYearDto.getCourseId());
    CourseYear courseYear = modelMapper.map(courseYearDto, CourseYear.class);
    courseYear.setCourse(course);
    courseYear.setId(null);
    CourseYear savedCourseYear = courseYearRepository.save(courseYear);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedCourseYear, username, AclPermission.ADMINISTRATION);
    return modelMapper.map(savedCourseYear, CouresYearDto.class);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.course.models.CourseYear', write) || hasRole('MANAGER')")
  public CouresYearDto updateCourseYear(Long id, CourseYearUpdateDto courseYearDto) {
    CourseYear courseYear = findCourseYear(id);
    modelMapper.map(courseYearDto, courseYear);
    return modelMapper.map(courseYearRepository.save(courseYear), CouresYearDto.class);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.course.models.CourseYear', delete) || hasRole('MANAGER')")
  public void deleteCourseYearById(Long id) {
    courseYearRepository.deleteById(id);
  }

}
