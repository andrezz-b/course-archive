package com.andrezzb.coursearchive.material.exceptions.tag;

import java.io.Serial;

public class TagException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public TagException(String message) {
    super(message);
  }

  public TagException(String message, Throwable cause) {
    super(message, cause);
  }
}
