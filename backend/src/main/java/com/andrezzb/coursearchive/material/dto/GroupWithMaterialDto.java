package com.andrezzb.coursearchive.material.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupWithMaterialDto extends MaterialGroupDto {
  private List<MaterialDto> materials;
}
