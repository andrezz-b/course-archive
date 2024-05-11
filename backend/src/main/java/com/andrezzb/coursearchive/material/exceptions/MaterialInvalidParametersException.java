package com.andrezzb.coursearchive.material.exceptions;

import java.io.Serial;

public class MaterialInvalidParametersException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public MaterialInvalidParametersException(String message) {
    super(message);
  }

  public MaterialInvalidParametersException(String message, Throwable cause) {
    super(message, cause);
  }
}
