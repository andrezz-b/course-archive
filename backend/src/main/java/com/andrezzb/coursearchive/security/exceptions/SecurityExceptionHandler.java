package com.andrezzb.coursearchive.security.exceptions;

import java.util.Collections;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.andrezzb.coursearchive.exceptions.ErrorObject;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler {

  @ExceptionHandler({EmailTakenException.class, UsernameTakenException.class})
  public ResponseEntity<ErrorObject> handleValueTakenException(RuntimeException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.BAD_REQUEST, errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorObject> handle(AuthenticationException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.UNAUTHORIZED, errors);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorObject);
  }

}
