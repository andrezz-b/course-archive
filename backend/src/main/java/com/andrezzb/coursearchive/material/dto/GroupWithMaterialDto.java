package com.andrezzb.coursearchive.material.dto;

import java.util.List;

import com.andrezzb.coursearchive.material.models.Material;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupWithMaterialDto extends MaterialGroupDto {
  private List<MaterialDto> materials;
}
