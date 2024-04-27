package com.andrezzb.coursearchive.material.exceptions;

import java.io.Serial;

public class MaterialGroupNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public MaterialGroupNotFoundException(Long id) {
    super("Material group not found with id: " + id.toString());
  }
}
