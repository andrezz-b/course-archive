package com.andrezzb.coursearchive.material.services;

import com.andrezzb.coursearchive.material.dto.MaterialDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.andrezzb.coursearchive.file.services.FileService;
import com.andrezzb.coursearchive.material.dto.MaterialCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialUpdateDto;
import com.andrezzb.coursearchive.material.exceptions.MaterialNotFoundException;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.repository.MaterialRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

import java.util.List;

@Service
public class MaterialService {
  private final MaterialRepository materialRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final MaterialGroupService materialGroupService;
  private final FileService fileService;

  public MaterialService(MaterialRepository materialRepository, AclUtilService aclUtilService,
      ModelMapper modelMapper, MaterialGroupService materialGroupService, FileService fileService) {
    this.materialRepository = materialRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.materialGroupService = materialGroupService;
    this.fileService = fileService;

    TypeMap<MaterialUpdateDto, Material> typeMap =
        this.modelMapper.createTypeMap(MaterialUpdateDto.class, Material.class);
    typeMap.addMappings(mapper -> mapper.skip(Material::setId));
  }

  @PreAuthorize("hasPermission(#courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', 'read') || hasRole('MANAGER')")
  public Page<MaterialDto> findAllMaterialsPaged(Pageable p, List<String> filterField, List<Object> filterValue,
      Long courseYearId, Long materialGroupId) {
    var materials = materialRepository.findAllByFilterFieldAndValue(p, filterField, filterValue,
        materialGroupId, courseYearId);
    return materials.map(material -> modelMapper.map(material, MaterialDto.class));
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.material.models.Material', 'read') || hasRole('MANAGER')")
  public Material findMaterialById(Long id) {
    return materialRepository.findById(id)
        .orElseThrow(() -> new MaterialNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#createDto.materialGroupId, 'com.andrezzb.coursearchive.material.models.MaterialGroup', 'create') || hasRole('MANAGER')")
  public Material createMaterial(MaterialCreateDto createDto, MultipartFile file) {
    var materialGroup = materialGroupService.findMaterialGroup(createDto.getMaterialGroupId());
    Material material = modelMapper.map(createDto, Material.class);
    material.setMaterialGroup(materialGroup);
    material.setId(null);
    Material savedMaterial = materialRepository.save(material);

    try {
      fileService.saveFile(file, savedMaterial);
    } catch (Exception e) {
      materialRepository.delete(savedMaterial);
      throw e;
    }

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedMaterial, username, AclPermission.ADMINISTRATION);
    return savedMaterial;
  }

  @Transactional
  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.material.models.Material', 'write') || hasRole('MANAGER')")
  public Material updateMaterial(Long id, MaterialUpdateDto updateDto) {
    Material material = findMaterialById(id);
    if (updateDto.getMaterialGroupId() != null) {
      var materialGroup =
          materialGroupService.findMaterialGroup(updateDto.getMaterialGroupId());
      material.setMaterialGroup(materialGroup);
    }
    modelMapper.map(updateDto, material);
    return materialRepository.save(material);
  }

  @Transactional
  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.material.models.Material', 'delete') || hasRole('MANAGER')")
  public void deleteMaterialById(Long id) {
    Material material;
    try {
      material = findMaterialById(id);
    } catch (Exception e) {
      // Ignore if doesn't exist
      return;
    }
    material.getFiles().forEach(fileService::deleteMaterialFile);
    materialRepository.delete(material);
  }


}
