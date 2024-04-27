package com.andrezzb.coursearchive.material.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.material.dto.MaterialGroupCreateDto;
import com.andrezzb.coursearchive.material.exceptions.MaterialGroupNotFoundException;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
import com.andrezzb.coursearchive.material.repository.MaterialGroupRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

@Service
public class MaterialGroupService {
  private final MaterialGroupRepository materialGroupRepository;
  private final AclUtilService aclUtilService;
  private final ModelMapper modelMapper;
  private final CourseYearService courseYearService;

  public MaterialGroupService(MaterialGroupRepository materialGroupRepository,
      AclUtilService aclUtilService, ModelMapper modelMapper, CourseYearService courseYearService) {
    this.materialGroupRepository = materialGroupRepository;
    this.aclUtilService = aclUtilService;
    this.modelMapper = modelMapper;
    this.courseYearService = courseYearService;
  }

  @PreAuthorize("hasRole('USER')")
  public Page<MaterialGroup> findAllMaterialGroupsPaged(Pageable p) {
    return materialGroupRepository.findAll(p);
  }

  @PreAuthorize("hasRole('USER')")
  public MaterialGroup findMaterialGroupById(Long id) {
    return materialGroupRepository.findById(id)
        .orElseThrow(() -> new MaterialGroupNotFoundException(id));
  }

  @Transactional
  @PreAuthorize("hasPermission(#materialGroupDto.courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', create) || hasRole('MANAGER')")
  public MaterialGroup createMaterialGroup(MaterialGroupCreateDto materialGroupDto) {
    var courseYear = courseYearService.findCourseYearById(materialGroupDto.getCourseYearId());

    if (materialGroupDto.getOrder() == null) {
      Short maxOrder = materialGroupRepository.findMaxOrder(courseYear.getId());
      materialGroupDto.setOrder(maxOrder != null ? (short) (maxOrder + 1) : 0);
    } else {
      materialGroupRepository.incrementOrder(courseYear.getId(), materialGroupDto.getOrder());
    }

    MaterialGroup materialGroup = modelMapper.map(materialGroupDto, MaterialGroup.class);
    materialGroup.setCourseYear(courseYear);
    MaterialGroup savedMaterialGroup = materialGroupRepository.save(materialGroup);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedMaterialGroup, username, AclPermission.ADMINISTRATION);
    return savedMaterialGroup;
  }

}
