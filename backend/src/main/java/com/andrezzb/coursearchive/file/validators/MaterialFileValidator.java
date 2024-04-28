package com.andrezzb.coursearchive.file.validators;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaterialFileValidator implements ConstraintValidator<MaterialFile, MultipartFile> {

  @Override
  public void initialize(MaterialFile constraintAnnotation) {}

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    String contentType = file.getContentType();
    return isSuppporedContentType(contentType);
  }

  private boolean isSuppporedContentType(String contentType) {
    return contentType.equals("text/plain")
        || contentType.equals("application/pdf")
        || contentType.equals("image/png")
        || contentType.equals("image/jpg")
        || contentType.equals("image/jpeg");
  }
}


