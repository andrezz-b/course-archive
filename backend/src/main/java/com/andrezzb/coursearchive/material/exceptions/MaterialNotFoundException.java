package com.andrezzb.coursearchive.material.exceptions;

import java.io.Serial;

public class MaterialNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public MaterialNotFoundException(Long id) {
    super("Material not found with id: " + id.toString());
  }
}
