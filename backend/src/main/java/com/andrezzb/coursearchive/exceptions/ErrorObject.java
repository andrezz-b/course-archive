package com.andrezzb.coursearchive.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
public class ErrorObject {
  private HttpStatus status;
  private Integer statusCode;
  private List<String> errors;
  private LocalDateTime timestamp;

  public ErrorObject(HttpStatus status, String error) {
    this(status, List.of(error));
  }

  public ErrorObject(HttpStatus status, List<String> errors) {
    this.status = status;
    this.statusCode = status.value();
    this.errors = errors;
    this.timestamp = LocalDateTime.now();
  }

  public ResponseEntity<ErrorObject> toResponseEntity() {
    return ResponseEntity.status(status).body(this);
  }
}
