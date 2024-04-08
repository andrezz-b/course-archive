package com.andrezzb.coursearchive.program.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgramCreateDto {

  @NotBlank(message = "Program name is required")
  @Size(max = 128, message = "Program name must be at most 128 characters")
  private String name;

  @NotNull(message = "College id is required")
  Long collegeId;

  @Min(value = 1, message = "Duration must be at least 1 year")
  private Short duration;

  @Size(max = 64, message = "Degree type must be at most 64 characters")
  private String degreeType;

  @Size(max = 128, message = "Degree title must be at most 128 characters")
  private String degreeTitle;

  @Size(max = 64, message = "Degree title abbreviation must be at most 64 characters")
  private String degreeTitleAbbreviation;

  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;
}
