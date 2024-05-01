package com.andrezzb.coursearchive.file.exceptions;

import com.andrezzb.coursearchive.material.models.Material;

public class MaterialFileNotFoundException extends RuntimeException {
  public MaterialFileNotFoundException(Long id) {
    super("Could not find material file " + id);
  }

  public MaterialFileNotFoundException(String message) {
    super(message);
  }

  public MaterialFileNotFoundException(Material material) {
    super("Could not find material file for material " + material.getId());
  }
}
