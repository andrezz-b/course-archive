package com.andrezzb.coursearchive.course.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.course.dto.CourseCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseDto;
import com.andrezzb.coursearchive.course.dto.CourseUpdateDto;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.services.CourseService;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.validators.ValidEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import static com.andrezzb.coursearchive.utils.PageRequestUtils.createPageRequest;

@RestController
@RequestMapping("api/course")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<CourseDto>> getAllCourses(
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "5") int size,
    @ValidEnum(enumClazz = Sort.Direction.class, ignoreCase = true)
    @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = Course.SortField.class) @RequestParam(defaultValue = "id")
    List<String> sortField, @ValidEnum(enumClazz = Course.FilterField.class, required = false)
  @RequestParam(required = false) List<String> filterField,
    @RequestParam(required = false) List<String> filterValue,
    @RequestParam(required = false) Long programId) {

    var filterValueObj =
      FilterValueMapper.mapFilterValue(Course.FilterField.class, filterField, filterValue);
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    final var coursesPaged =
      courseService.findAllCoursesPaged(p, filterField, filterValueObj, programId);
    return ResponseEntity.ok(coursesPaged);
  }

  @PostMapping("/")
  public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseCreateDto courseCreateDto) {
    var course = courseService.createCourse(courseCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(course);
  }

  @GetMapping("/{id}")
  private ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
    final var course = courseService.findCourseById(id);
    return ResponseEntity.ok(courseService.mapCourseToDto(course));
  }

  @PutMapping("/{id}")
  private ResponseEntity<CourseDto> updateCourseById(@PathVariable Long id,
    @Valid @RequestBody CourseUpdateDto courseUpdateDto) {
    var course = courseService.updatCourse(id, courseUpdateDto);
    return ResponseEntity.ok(course);
  }

  @DeleteMapping("/{id}")
  private ResponseEntity<Void> deleteCourseById(@PathVariable Long id) {
    courseService.deleteCourseById(id);
    return ResponseEntity.noContent().build();
  }

}
