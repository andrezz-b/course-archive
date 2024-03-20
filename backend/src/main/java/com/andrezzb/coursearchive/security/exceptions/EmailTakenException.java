package com.andrezzb.coursearchive.security.exceptions;

import java.io.Serial;

public class EmailTakenException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public EmailTakenException(String email) {
    super("Email " + email + " is already taken.");
  }

}
