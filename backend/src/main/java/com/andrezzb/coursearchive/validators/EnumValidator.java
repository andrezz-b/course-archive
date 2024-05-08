package com.andrezzb.coursearchive.validators;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

  List<String> valueList = null;
  Boolean required;
  Boolean ignoreCase;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    if (!required && value == null) {
      return true;
    }
    if (value == null) {
      context.buildConstraintViolationWithTemplate("Value is required").addConstraintViolation();
      return false;
    }
    String valueStr = ignoreCase ? value.toUpperCase() : value;
    Boolean isValid = valueList.contains(valueStr);
    if (!isValid) {
      context.buildConstraintViolationWithTemplate(generatedAcceptedValuesMessage()).addConstraintViolation();
    }
    return isValid;
  }

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    required = constraintAnnotation.required();
    ignoreCase = constraintAnnotation.ignoreCase();
    valueList = new ArrayList<String>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for (@SuppressWarnings("rawtypes")
    Enum enumVal : enumValArr) {
      var enumValueStr = ignoreCase ? enumVal.toString().toUpperCase() : enumVal.toString();
      valueList.add(enumValueStr);
    }
  }

  private String generatedAcceptedValuesMessage() {
    String caseNote = this.ignoreCase ? " (case insensitive)" : "";
    String message =  "Invalid value. Accepted values are" + caseNote + ": " + String.join(", ", valueList);
    return message;
}

}
