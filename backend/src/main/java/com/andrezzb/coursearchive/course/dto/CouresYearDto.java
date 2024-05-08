package com.andrezzb.coursearchive.course.dto;

import lombok.Data;

@Data
public class CouresYearDto {
  private Long id;
  private String academicYear;
  private String professor;
  private String assistant;
  private Short difficulty;
  private Short enrollmentCount;
  private Short passedCount;
  private Short lectureCount;
  private Short exerciseCount;
  private Short laboratoryCount;
}
