package com.andrezzb.coursearchive.course.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.course.dto.CourseCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseUpdateDto;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.course.services.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/course")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<Course>> getAllCourses(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(defaultValue = "id") String sortField) {
    Sort.Direction direction =
        sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable p = PageRequest.of(page, size, Sort.by(direction, sortField));
    final var coursesPaged = courseService.findAllCoursesPaged(p);
    return ResponseEntity.ok(coursesPaged);
  }

  @PostMapping("/")
  public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseCreateDto courseCreateDto) {
    var course = courseService.createCourse(courseCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(course);
  }

  @GetMapping("/{id}")
  private ResponseEntity<Course> getCourseById(@PathVariable Long id) {
    final var course = courseService.findCourseById(id);
    return ResponseEntity.ok(course);
  }

  @PutMapping("/{id}")
  private ResponseEntity<Course> updateCourseById(@PathVariable Long id,
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
