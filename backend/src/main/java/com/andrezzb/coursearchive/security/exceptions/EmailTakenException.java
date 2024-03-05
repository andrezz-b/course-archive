package com.andrezzb.coursearchive.security.exceptions;

public class EmailTakenException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public EmailTakenException(String email) {
    super("Email " + email + " is already taken.");
  }

}
