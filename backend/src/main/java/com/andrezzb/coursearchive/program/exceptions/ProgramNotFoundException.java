package com.andrezzb.coursearchive.program.exceptions;

import java.io.Serial;

public class ProgramNotFoundException extends RuntimeException {
      @Serial
    private static final long serialVersionUID = 1L;

    public ProgramNotFoundException(Long id) {
        super("Program not found with id: " + id.toString());
    }
}
