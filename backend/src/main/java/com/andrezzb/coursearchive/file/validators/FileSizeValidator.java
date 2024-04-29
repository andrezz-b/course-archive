package com.andrezzb.coursearchive.file.validators;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

  private long maxSize;
  
  @Override
  public void initialize(FileSize constraintAnnotation) {
    this.maxSize = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    return value.getSize() <= maxSize;
  }
  
}
