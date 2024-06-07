package com.andrezzb.coursearchive.material.exceptions;

import java.io.Serial;

public class CommentNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public CommentNotFoundException(Long id) {
    super("Comment not found with id: " + id.toString());
  }

  public CommentNotFoundException(String message) {
    super(message);
  }
}
