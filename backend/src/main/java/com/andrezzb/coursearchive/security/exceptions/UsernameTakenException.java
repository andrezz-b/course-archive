package com.andrezzb.coursearchive.security.exceptions;

public class UsernameTakenException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UsernameTakenException(String username) {
    super("Username " + username + " is already taken.");
  }
  
}
