package com.andrezzb.coursearchive.college.exceptions;

import com.andrezzb.coursearchive.exceptions.ErrorObject;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CollegeExceptionHandler {

    @ExceptionHandler(CollegeNotFoundException.class)
    public ResponseEntity<ErrorObject> handle(CollegeNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        final ErrorObject errorObject = new ErrorObject(HttpStatus.NOT_FOUND, errors);

        return ResponseEntity.status(errorObject.getStatus()).body(errorObject);
    }
}
