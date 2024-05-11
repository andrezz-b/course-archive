package com.andrezzb.coursearchive.material.exceptions;

import java.util.Collections;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.andrezzb.coursearchive.exceptions.ErrorObject;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MaterialExceptionHandler {

  @ExceptionHandler({MaterialNotFoundException.class, MaterialGroupNotFoundException.class})
  public ResponseEntity<ErrorObject> handle(RuntimeException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.NOT_FOUND, errors);

    return ResponseEntity.status(errorObject.getStatus()).body(errorObject);
  }
}
