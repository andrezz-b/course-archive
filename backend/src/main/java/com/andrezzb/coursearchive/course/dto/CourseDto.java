package com.andrezzb.coursearchive.course.dto;

import lombok.Data;

@Data
public class CourseDto {
  private Long id;
  private String name;
  private Short credits;
  private Short year;
  private String acronym;
  private String description;
}
