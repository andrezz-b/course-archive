package com.andrezzb.coursearchive.course.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.validators.ValidEnum;
import com.andrezzb.coursearchive.course.dto.CouresYearDto;
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
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
  public ResponseEntity<Page<CouresYearDto>> getAllCourseYears(
      @PositiveOrZero @RequestParam(defaultValue = "0") int page,
      @Positive @RequestParam(defaultValue = "5") int size,
      @ValidEnum(enumClazz = Sort.Direction.class,
          ignoreCase = true) @RequestParam(defaultValue = "desc") String sortDirection,
      @ValidEnum(enumClazz = CourseYear.SortField.class) @RequestParam(
          defaultValue = "academicYear") String sortField,
      @ValidEnum(enumClazz = CourseYear.FilterField.class, required = false) @RequestParam(
          required = false) String filterField,
      @RequestParam(required = false) String filterValue,
      @RequestParam(required = false) Long courseId) {

    Object filterValueObj = FilterValueMapper.mapFilterValue(CourseYear.FilterField.class, filterField, filterValue);
    Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
    final var courseYearsPaged = courseYearService.findAllCourseYearsPaged(p, filterField, filterValueObj, courseId);
    return ResponseEntity.ok(courseYearsPaged);
  }

  @PostMapping("/")
  public ResponseEntity<CouresYearDto> createCourseYear(
      @Valid @RequestBody CourseYearCreateDto createDto) {
    var courseYear = courseYearService.createCourseYear(createDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(courseYear);
  }

  @GetMapping("/{id}")
  private ResponseEntity<CouresYearDto> getCourseYearById(@PathVariable Long id) {
    final var courseYear = courseYearService.findCourseYearById(id);
    return ResponseEntity.ok(courseYear);
  }

  @PutMapping("/{id}")
  private ResponseEntity<CouresYearDto> updateCourseYearById(@PathVariable Long id,
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
