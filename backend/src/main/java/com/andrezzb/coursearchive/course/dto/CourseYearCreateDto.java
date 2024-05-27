package com.andrezzb.coursearchive.course.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseYearCreateDto {

    @NotNull(message = "Course id is required")
    private Long courseId;

    @NotBlank(message = "Year is required")
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "Year must be in the format 'yyyy/yyyy'")
    private String academicYear;

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

    @AssertTrue(message = "Year must be in the format 'yyyy/yyyy', difference must be 1 year")
    public boolean isDifferenceCorrect() {
        if (academicYear == null) {
            return false;
        }

        String[] parts = academicYear.split("/");
        if (parts.length != 2) {
            return false;
        }

        try {
            int startYear = Integer.parseInt(parts[0]);
            int endYear = Integer.parseInt(parts[1]);

            return startYear + 1 == endYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}