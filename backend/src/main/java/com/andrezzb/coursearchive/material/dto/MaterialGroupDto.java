package com.andrezzb.coursearchive.material.dto;

import lombok.Data;

@Data
public class MaterialGroupDto {
  private Long id;
  private String name;
  private Short displayOrder;
  private String description;
}
