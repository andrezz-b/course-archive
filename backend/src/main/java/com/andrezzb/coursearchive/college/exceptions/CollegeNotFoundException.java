package com.andrezzb.coursearchive.college.exceptions;

import java.io.Serial;

public class CollegeNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CollegeNotFoundException(Long id) {
        super("College not found with id: " + id.toString());
    }
}
