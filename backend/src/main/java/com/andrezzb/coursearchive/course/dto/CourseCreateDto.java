package com.andrezzb.coursearchive.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseCreateDto {

    @NotNull(message = "Program id is required")
    private Long programId;

    @NotBlank(message = "Course name is required")
    @Size(max = 64, message = "Course name must be at most 64 characters")
    private String name;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Short credits;

    @Min(value = 1, message = "Year must be at least 1")
    private Short year;

    @Size(max = 16, message = "Acronym must be at most 16 characters")
    private String acronym;

    @Size(max = 512, message = "Description must be at most 512 characters")
    private String description;
}
