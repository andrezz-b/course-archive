package com.andrezzb.coursearchive.material.controllers;

import com.andrezzb.coursearchive.material.dto.MaterialDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.file.services.FileService;
import com.andrezzb.coursearchive.file.validators.ValidMaterialFile;
import com.andrezzb.coursearchive.material.dto.MaterialCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialUpdateDto;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.services.MaterialService;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

import static com.andrezzb.coursearchive.utils.PageRequestUtils.createPageRequest;

@RestController
@RequestMapping("api/material")
public class MaterialController {
  private final MaterialService materialService;
  private final FileService fileService;

  public MaterialController(MaterialService materialService, FileService fileService) {
    this.materialService = materialService;
    this.fileService = fileService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<MaterialDto>> getAllMaterials(
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "5") int size,
    @ValidEnum(enumClazz = Sort.Direction.class, ignoreCase = true)
    @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = Material.SortField.class) @RequestParam(defaultValue = "name")
    List<String> sortField, @ValidEnum(enumClazz = Material.FilterField.class, required = false)
  @RequestParam(required = false) List<String> filterField,
    @RequestParam(required = false) List<String> filterValue, @RequestParam() Long courseYearId,
    @RequestParam(required = false) Long materialGroupId) {
    var filterValueObj =
      FilterValueMapper.mapFilterValue(Material.FilterField.class, filterField, filterValue);
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    final var materialsPaged =
      materialService.findAllMaterialsPaged(p, filterField, filterValueObj, courseYearId,
        materialGroupId);
    return ResponseEntity.ok(materialsPaged);
  }

  @PostMapping(path = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Material> createMaterial(
    @Valid @RequestPart("material") MaterialCreateDto materialCreateDto,
    @ValidMaterialFile @RequestPart("file") MultipartFile file) {
    var material = materialService.createMaterial(materialCreateDto, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(material);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
    var material = materialService.findMaterialById(id);
    return ResponseEntity.ok(material);
  }

  @GetMapping("/file/{materialId}")
  public ResponseEntity<Resource> getMaterialFileById(@PathVariable Long materialId) {
    MaterialFile materialFile = fileService.findMaterialFileByMaterialId(materialId);
    Resource fileResource = fileService.loadMaterialFileResource(materialFile);

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(materialFile.getMimeType()))
      .body(fileResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Material> updateMaterialById(@PathVariable Long id,
    @Valid @RequestBody MaterialUpdateDto materialUpdateDto) {
    var material = materialService.updateMaterial(id, materialUpdateDto);
    return ResponseEntity.ok(material);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMaterialById(@PathVariable Long id) {
    materialService.deleteMaterialById(id);
    return ResponseEntity.noContent().build();
  }

}
