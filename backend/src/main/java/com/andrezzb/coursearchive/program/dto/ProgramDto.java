package com.andrezzb.coursearchive.program.dto;

import lombok.Data;

@Data
public class ProgramDto {
  private Long id;
  private String name;
  private Short duration;
  private String degreeType;
  private String degreeTitle;
  private String degreeTitleAbbreviation;
  private String description;
}
