package com.andrezzb.coursearchive.course.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.course.dto.CourseYearCreateDto;
import com.andrezzb.coursearchive.course.dto.CourseYearUpdateDto;
import com.andrezzb.coursearchive.course.models.CourseYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/course/year")
public class CourseYearController {

  private final CourseYearService courseYearService;

  public CourseYearController(CourseYearService courseYearService) {
    this.courseYearService = courseYearService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<CourseYear>> getAllCourseYears(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(defaultValue = "id") String sortField) {
    Sort.Direction direction =
        sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable p = PageRequest.of(page, size, Sort.by(direction, sortField));
    final var courseYearsPaged = courseYearService.findAllCourseYearsPaged(p);
    return ResponseEntity.ok(courseYearsPaged);
  }

  @PostMapping("/")
  public ResponseEntity<CourseYear> createCourseYear(
      @Valid @RequestBody CourseYearCreateDto createDto) {
    var courseYear = courseYearService.createCourseYear(createDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(courseYear);
  }

  @GetMapping("/{id}")
  private ResponseEntity<CourseYear> getCourseYearById(@PathVariable Long id) {
    final var courseYear = courseYearService.findCourseYearById(id);
    return ResponseEntity.ok(courseYear);
  }

  @PutMapping("/{id}")
  private ResponseEntity<CourseYear> updateCourseYearById(@PathVariable Long id,
      @Valid @RequestBody CourseYearUpdateDto updateDto) {
    final var courseYear = courseYearService.updateCourseYear(id, updateDto);
    return ResponseEntity.ok(courseYear);
  }

  @DeleteMapping("/{id}")
  private ResponseEntity<Void> deleteCourseYearById(@PathVariable Long id) {
    courseYearService.deleteCourseYearById(id);
    return ResponseEntity.noContent().build();
  }

}
