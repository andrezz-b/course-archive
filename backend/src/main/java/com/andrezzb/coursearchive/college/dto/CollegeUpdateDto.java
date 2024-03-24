package com.andrezzb.coursearchive.college.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollegeUpdateDto {
  private String acronym;
  private String city;
  private Integer postcode;
  private String address;
  private String website;
  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;
}
