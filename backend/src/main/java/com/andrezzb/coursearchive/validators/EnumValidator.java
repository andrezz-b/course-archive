package com.andrezzb.coursearchive.validators;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<ValidEnum, Object> {

  List<String> valueList = null;
  Boolean required;
  Boolean ignoreCase;

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    if (!required && value == null) {
      return true;
    }
    if (value == null) {
      context.buildConstraintViolationWithTemplate("Value is required").addConstraintViolation();
      return false;
    }
    boolean isValid = isValidValue(value);
    if (!isValid) {
      context.buildConstraintViolationWithTemplate(generatedAcceptedValuesMessage())
        .addConstraintViolation();
    }
    return isValid;
  }

  private boolean isValidValue(Object value) {
    switch (value) {
      case String s -> {
        String valueStr = ignoreCase ? s.toUpperCase() : s;
        return valueList.contains(valueStr);
      }
      case String[] strings -> {
        for (String str : strings) {
          String valueStr = ignoreCase ? str.toUpperCase() : str;
          if (!valueList.contains(valueStr)) {
            return false;
          }
        }
      }
      case @SuppressWarnings("rawtypes")List list -> {
        for (Object obj : list) {
          if (obj instanceof String) {
            String valueStr = ignoreCase ? ((String) obj).toUpperCase() : (String) obj;
            if (!valueList.contains(valueStr)) {
              return false;
            }
          } else {
            throw new IllegalArgumentException(
              "Unsupported data type in list. Only String is supported.");
          }
        }
      }
      case null, default -> throw new IllegalArgumentException(
        "Unsupported data type. Only String, String[] and List<String> are supported.");
    }
    return true;
  }

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    required = constraintAnnotation.required();
    ignoreCase = constraintAnnotation.ignoreCase();
    valueList = new ArrayList<>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
      var enumValueStr = ignoreCase ? enumVal.toString().toUpperCase() : enumVal.toString();
      valueList.add(enumValueStr);
    }
  }

  private String generatedAcceptedValuesMessage() {
    String caseNote = this.ignoreCase ? " (case insensitive)" : "";
    return "Invalid value. Accepted values are" + caseNote + ": " + String.join(", ", valueList);
  }

}
