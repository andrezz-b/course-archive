package com.andrezzb.coursearchive.course.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.dto.CourseYearCreateDto;
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
  public Page<CourseYear> findAllCourseYearsPaged(Pageable p) {
    return courseYearRepository.findAll(p);
  }

  @PreAuthorize("hasRole('USER')")
  public CourseYear findCourseYearById(Long id) {
    return courseYearRepository.findById(id).orElseThrow(() -> new CourseYearNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#courseYearDto.courseId, 'com.andrezzb.coursearchive.course.models.Course', create) || hasRole('MANAGER')")
  public CourseYear createCourseYear(CourseYearCreateDto courseYearDto) {
    var course = courseService.findCourseById(courseYearDto.getCourseId());
    CourseYear courseYear = modelMapper.map(courseYearDto, CourseYear.class);
    courseYear.setCourse(course);
    CourseYear savedCourseYear = courseYearRepository.save(courseYear);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedCourseYear, username, AclPermission.ADMINISTRATION);
    return savedCourseYear;
  }

}
