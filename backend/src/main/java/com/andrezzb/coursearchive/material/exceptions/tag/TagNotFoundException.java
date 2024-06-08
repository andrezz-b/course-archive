package com.andrezzb.coursearchive.material.exceptions.tag;

public class TagNotFoundException extends TagException {
  public TagNotFoundException(String message) {
    super(message);
  }

  public TagNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
