package com.andrezzb.coursearchive.file.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MaterialFileValidator.class})
public @interface MaterialFile {
  String message() default "Only PDF,XML,PNG or JPG images are allowed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
