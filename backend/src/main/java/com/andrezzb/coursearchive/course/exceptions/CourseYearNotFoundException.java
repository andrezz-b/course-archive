package com.andrezzb.coursearchive.course.exceptions;

import java.io.Serial;

public class CourseYearNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public CourseYearNotFoundException(Long id) {
    super("CourseYear not found with id: " + id.toString());
  }

}
