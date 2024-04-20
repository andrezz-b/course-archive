package com.andrezzb.coursearchive.course.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.course.services.CourseYearService;

@RestController
@RequestMapping("api/course-year")
public class CourseYearController {
  
  private final CourseYearService courseYearService;

  public CourseYearController(CourseYearService courseYearService) {
    this.courseYearService = courseYearService;
  }
}
