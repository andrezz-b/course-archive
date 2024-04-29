package com.andrezzb.coursearchive.file.validators;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

  private List<String> allowedFileTypes = null;

  @Override
  public void initialize(FileType constraintAnnotation) {
    this.allowedFileTypes = Arrays.asList(constraintAnnotation.value());
  }

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    Tika tika = new Tika();
    boolean isSupported = true;
    context.disableDefaultConstraintViolation();

    try {
      String mimeType = tika.detect(value.getBytes());
      if (!allowedFileTypes.contains(mimeType)) {
        isSupported = false;
        context.buildConstraintViolationWithTemplate(generateUnsupportedFileTypeMessage(mimeType))
            .addConstraintViolation();
      }
    } catch (IOException e) {
      isSupported = false;
      context.buildConstraintViolationWithTemplate("Error reading file").addConstraintViolation();
    }

    return isSupported;
  }

  private String generateUnsupportedFileTypeMessage(String detectedFileType) {
    return detectedFileType + " is not supported. Only " + String.join(", ", allowedFileTypes)
        + " files are allowed";
  }
}
