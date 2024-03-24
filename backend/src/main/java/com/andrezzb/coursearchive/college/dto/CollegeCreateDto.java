package com.andrezzb.coursearchive.college.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollegeCreateDto {

  @NotBlank(message = "College name is required")
  @Size(max = 128, message = "College name must be at most 128 characters")
  private String name;

  private String acronym;

  @NotBlank(message = "City is required")
  private String city;
  @NotNull(message = "Postcode is required")
  private Integer postcode;
  @NotBlank(message = "Address is required")
  private String address;

  private String website;
  @Size(max = 512, message = "Description must be at most 512 characters")
  private String description;

}
