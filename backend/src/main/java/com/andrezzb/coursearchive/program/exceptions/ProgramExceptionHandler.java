package com.andrezzb.coursearchive.program.exceptions;

import com.andrezzb.coursearchive.exceptions.ErrorObject;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProgramExceptionHandler {
    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<ErrorObject> handle(ProgramNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        final ErrorObject errorObject = new ErrorObject(HttpStatus.NOT_FOUND, errors);

        return ResponseEntity.status(errorObject.getStatus()).body(errorObject);
    }
}
