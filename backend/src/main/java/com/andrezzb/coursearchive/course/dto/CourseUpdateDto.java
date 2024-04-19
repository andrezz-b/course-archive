package com.andrezzb.coursearchive.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseUpdateDto {
    @Size(max = 128, message = "Course name must be at most 128 characters")
    private String name;

    @Min(value = 1, message = "Credits must be at least 1")
    private Short credits;

    @Min(value = 1, message = "Year must be at least 1")
    private Short year;

    @Size(max = 32, message = "Acronym must be at most 32 characters")
    private String acronym;

    @Size(max = 512, message = "Description must be at most 512 characters")
    private String description;
}