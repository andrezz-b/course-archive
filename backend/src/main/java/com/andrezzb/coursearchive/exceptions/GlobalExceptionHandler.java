package com.andrezzb.coursearchive.exceptions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Order()
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorObject> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.BAD_REQUEST, errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorObject> handleMissingRequestParamter(MissingServletRequestParameterException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.BAD_REQUEST, errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorObject> handleGeneralExceptions(Exception ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    final ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR, errors);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorObject);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ErrorObject> onHandlerMethodValidationException(
      HandlerMethodValidationException e) {
    // Map errors with fieldName: error1, error2, ...
    List<String> errors = e.getAllValidationResults().stream()
        .map(error -> error.getMethodParameter().getParameterName()
            + ": "
            + error.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")))
        .collect(Collectors.toList());
    final ErrorObject error = new ErrorObject(HttpStatus.BAD_REQUEST, errors);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

}
