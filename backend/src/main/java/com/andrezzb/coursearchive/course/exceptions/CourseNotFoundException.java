package com.andrezzb.coursearchive.course.exceptions;

import java.io.Serial;

public class CourseNotFoundException extends RuntimeException{
  @Serial
  private static final long serialVersionUID = 1L;

  public CourseNotFoundException(Long id) {
      super("Course not found with id: " + id.toString());
  }
  
}
