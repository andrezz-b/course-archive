package com.andrezzb.coursearchive.material.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.andrezzb.coursearchive.file.services.FileService;
import com.andrezzb.coursearchive.file.validators.ValidMaterialFile;
import com.andrezzb.coursearchive.material.dto.MaterialCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialUpdateDto;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.services.MaterialService;
import jakarta.validation.Valid;

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
  public ResponseEntity<Page<Material>> getAllMaterials(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(defaultValue = "name") String sortField) {
    Sort.Direction direction =
        sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable p = PageRequest.of(page, size, Sort.by(direction, sortField));
    final var materialsPaged = materialService.findAllMaterialsPaged(p);
    return ResponseEntity.ok(materialsPaged);
  }

  @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

  @GetMapping("/file/{id}")
  public ResponseEntity<?> getMaterialFileById(@PathVariable Long id) {
    var material = materialService.findMaterialById(id);
    var fileDto = fileService.retreiveMaterialFile(material);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(fileDto.getMaterialFile().getMimeType()))
        .body(fileDto.getFileResource());
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
