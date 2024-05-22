package com.andrezzb.coursearchive.security.exceptions;

import java.io.Serial;

public class UsernameTakenException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public UsernameTakenException(String username) {
    super("Username " + username + " is already taken.");
  }
  
}
