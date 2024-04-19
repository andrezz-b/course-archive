package com.andrezzb.coursearchive.course.exceptions;

import java.util.Collections;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.andrezzb.coursearchive.exceptions.ErrorObject;
import org.springframework.core.Ordered;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CourseExceptionHandler {

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<ErrorObject> handle(CourseNotFoundException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.NOT_FOUND, errors);

    return ResponseEntity.status(errorObject.getStatus()).body(errorObject);
  }

}
