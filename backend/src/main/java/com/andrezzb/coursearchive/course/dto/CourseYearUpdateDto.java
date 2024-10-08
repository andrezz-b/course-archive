package com.andrezzb.coursearchive.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseYearUpdateDto {
    @Size(max = 256, message = "Professor names must be at most 256 characters")
    private String professor;

    @Size(max = 256, message = "Assistant names must be at most 256 characters")
    private String assistant;

    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 10, message = "Difficulty must be at most 10")
    private Short difficulty;

    private Short enrollmentCount;
    private Short passedCount;

    @Min(value = 1, message = "Lecture count must be at least 1")
    private Short lectureCount;

    @Min(value = 1, message = "Exercise count must be at least 1")
    private Short exerciseCount;

    @Min(value = 1, message = "Laboratory count must be at least 1")
    private Short laboratoryCount;
}
