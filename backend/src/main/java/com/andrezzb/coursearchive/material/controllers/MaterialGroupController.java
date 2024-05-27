package com.andrezzb.coursearchive.material.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andrezzb.coursearchive.material.dto.GroupWithMaterialDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupUpdateDto;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
import com.andrezzb.coursearchive.material.services.MaterialGroupService;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/material/group")
public class MaterialGroupController {
  private final MaterialGroupService materialGroupService;

  public MaterialGroupController(MaterialGroupService materialGroupService) {
    this.materialGroupService = materialGroupService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<GroupWithMaterialDto>> getAllMaterialGroups(
      @PositiveOrZero @RequestParam(defaultValue = "0") int page,
      @Positive @RequestParam(defaultValue = "5") int size,
      @ValidEnum(enumClazz = Sort.Direction.class,
          ignoreCase = true) @RequestParam(defaultValue = "asc") String sortDirection,
      @ValidEnum(enumClazz = MaterialGroup.SortField.class) @RequestParam(
          defaultValue = "displayOrder") String sortField,
      @ValidEnum(enumClazz = MaterialGroup.FilterField.class, required = false) @RequestParam(
          required = false) String filterField,
      @RequestParam(required = false) String filterValue,
      @RequestParam Long courseYearId) {

    Object filterValueObj =
        FilterValueMapper.mapFilterValue(MaterialGroup.FilterField.class, filterField, filterValue);
    Pageable p =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
    final var materialGroupsPaged = materialGroupService.findAllMaterialGroupsPaged(p, filterField,
        filterValueObj, courseYearId);
    return ResponseEntity.ok(materialGroupsPaged);
  }

  @PostMapping("/")
  public ResponseEntity<MaterialGroupDto> createMaterialGroup(
      @Valid @RequestBody MaterialGroupCreateDto materialGroupCreateDto) {
    var materialGroup = materialGroupService.createMaterialGroup(materialGroupCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(materialGroup);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MaterialGroupDto> updateMaterailGroupById(@PathVariable Long id,
      @Valid @RequestBody MaterialGroupUpdateDto materialGroupUpdateDto) {
    var materialGroup = materialGroupService.updateMaterialGroup(id, materialGroupUpdateDto);
    return ResponseEntity.ok(materialGroup);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMaterialGroupById(@PathVariable Long id) {
    materialGroupService.deleteMaterialGroupById(id);
    return ResponseEntity.noContent().build();
  }
}
