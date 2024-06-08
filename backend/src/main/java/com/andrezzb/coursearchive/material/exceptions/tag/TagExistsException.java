package com.andrezzb.coursearchive.material.exceptions.tag;

public class TagExistsException extends TagException {
  public TagExistsException(String message) {
    super(message);
  }

  public TagExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
