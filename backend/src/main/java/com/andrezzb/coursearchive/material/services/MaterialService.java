package com.andrezzb.coursearchive.material.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.material.dto.MaterialCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialUpdateDto;
import com.andrezzb.coursearchive.material.exceptions.MaterialNotFoundException;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.repository.MaterialRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

@Service
public class MaterialService {
  private final MaterialRepository materialRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final MaterialGroupService materialGroupService;

  public MaterialService(MaterialRepository materialRepository, AclUtilService aclUtilService,
      ModelMapper modelMapper, MaterialGroupService materialGroupService) {
    this.materialRepository = materialRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.materialGroupService = materialGroupService;

    TypeMap<MaterialUpdateDto, Material> typeMap =
        this.modelMapper.createTypeMap(MaterialUpdateDto.class, Material.class);
    typeMap.addMappings(mapper -> mapper.skip(Material::setId));
  }

  @PreAuthorize("hasRole('USER')")
  public Page<Material> findAllMaterialsPaged(Pageable p) {
    return materialRepository.findAll(p);
  }

  @PreAuthorize("hasRole('USER')")
  public Material findMaterialById(Long id) {
    return materialRepository.findById(id)
        .orElseThrow(() -> new MaterialNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#createDto.materialGroupId, 'com.andrezzb.coursearchive.material.models.MaterialGroup', create) || hasRole('MANAGER')")
  public Material createMaterial(MaterialCreateDto createDto) {
    var materialGroup = materialGroupService.findMaterialGroupById(createDto.getMaterialGroupId());
    Material material = modelMapper.map(createDto, Material.class);
    material.setMaterialGroup(materialGroup);
    material.setId(null);
    Material savedMaterial = materialRepository.save(material);

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
          materialGroupService.findMaterialGroupById(updateDto.getMaterialGroupId());
      material.setMaterialGroup(materialGroup);
    }
    modelMapper.map(updateDto, material);
    return materialRepository.save(material);
  }

  @PreAuthorize("hasPermission(#id, 'com.andrezzb.coursearchive.material.models.Material', 'delete') || hasRole('MANAGER')")
  public void deleteMaterialById(Long id) {
    materialRepository.deleteById(id);
  }


}
