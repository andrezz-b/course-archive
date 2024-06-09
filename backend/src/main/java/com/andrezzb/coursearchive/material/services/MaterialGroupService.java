package com.andrezzb.coursearchive.material.services;

import org.apache.tika.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.material.dto.GroupWithMaterialDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupCreateDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupDto;
import com.andrezzb.coursearchive.material.dto.MaterialGroupUpdateDto;
import com.andrezzb.coursearchive.material.exceptions.MaterialGroupNotFoundException;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
import com.andrezzb.coursearchive.material.repository.MaterialGroupRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;

import java.util.List;

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

  @PreAuthorize(
    "hasPermission(#courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', 'read') || hasRole('MANAGER')")
  public Page<GroupWithMaterialDto> findAllMaterialGroupsPaged(Pageable p, List<String> filterField,
    List<Object> filterValue, Long courseYearId) {
    var groups = materialGroupRepository.findAllByFilterFiledAndValue(p, filterField, filterValue,
      courseYearId);
    return groups.map(group -> modelMapper.map(group, GroupWithMaterialDto.class));
  }

  @PreAuthorize(
    "hasPermission(#id, 'com.andrezzb.coursearchive.material.models.MaterialGroup', 'read') || hasRole('MANAGER')")
  public MaterialGroup findMaterialGroup(Long id) {
    return materialGroupRepository.findById(id)
      .orElseThrow(() -> new MaterialGroupNotFoundException(id));
  }

  @Transactional
  @PreAuthorize(
    "hasPermission(#materialGroupDto.courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', 'create') || hasRole('MANAGER')")
  public MaterialGroupDto createMaterialGroup(MaterialGroupCreateDto materialGroupDto) {
    var courseYear = courseYearService.findCourseYear(materialGroupDto.getCourseYearId());

    Short displayOrder =
      getNewDisplayOrder(courseYear.getId(), null, materialGroupDto.getDisplayOrder(), false);
    materialGroupDto.setDisplayOrder(displayOrder);

    MaterialGroup materialGroup = modelMapper.map(materialGroupDto, MaterialGroup.class);
    materialGroup.setCourseYear(courseYear);
    materialGroup.setId(null);
    MaterialGroup savedMaterialGroup = materialGroupRepository.save(materialGroup);

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedMaterialGroup, username, AclPermission.ADMINISTRATION);
    return modelMapper.map(savedMaterialGroup, MaterialGroupDto.class);
  }

  @Transactional
  @PreAuthorize(
    "hasPermission(#id, 'com.andrezzb.coursearchive.material.models.MaterialGroup', 'write') || hasRole('MANAGER')")
  public MaterialGroupDto updateMaterialGroup(Long id, MaterialGroupUpdateDto updateDto) {
    MaterialGroup materialGroup = findMaterialGroup(id);
    Short displayOrder =
      getNewDisplayOrder(materialGroup.getCourseYear().getId(), materialGroup.getDisplayOrder(),
        updateDto.getDisplayOrder(), true);
    updateDto.setDisplayOrder(displayOrder);
    modelMapper.map(updateDto, materialGroup);
    var savedGroup = materialGroupRepository.save(materialGroup);
    return modelMapper.map(savedGroup, MaterialGroupDto.class);
  }

  @PreAuthorize(
    "hasPermission(#id, 'com.andrezzb.coursearchive.material.models.MaterialGroup', 'delete') || hasRole('MANAGER')")
  public void deleteMaterialGroupById(Long id) {
    MaterialGroup group;
    try {
      group = findMaterialGroup(id);
    } catch (Exception e) {
      return;
    }
    if (!group.getMaterials().isEmpty()) {
      throw new IllegalStateException("Group has materials, cannot delete");
    }
    materialGroupRepository.deleteById(id);
  }

  public static Page<GroupWithMaterialDto> filterMaterials(Page<GroupWithMaterialDto> materialGroupsPaged,
    String materialName, List<Long> tagIds) {

    for (GroupWithMaterialDto group : materialGroupsPaged.getContent()) {
      var materialStream = group.getMaterials().stream();

      if (!StringUtils.isBlank(materialName)) {
        materialStream =
          materialStream.filter(material -> material.getName().contains(materialName));
      }

      if (tagIds != null && !tagIds.isEmpty()) {
        materialStream = materialStream.filter(
          material -> tagIds.stream().allMatch(tagId -> material.getTags().stream().anyMatch(tag -> tag.getId().equals(tagId)))
        );
      }
      group.setMaterials(materialStream.toList());
    }

    return materialGroupsPaged;
  }

  Short getNewDisplayOrder(Long courseYearId, Short oldOrder, Short newOrder) {
    return getNewDisplayOrder(courseYearId, oldOrder, newOrder, true);
  }

  Short getNewDisplayOrder(Long courseYearId, Short oldOrder, Short newOrder, boolean exists) {
    long groupCount = materialGroupRepository.countByCourseYearId(courseYearId).orElse(0L);
    if (groupCount <= 1 && exists) {
      return 0;
    } else if (groupCount <= 1) {
      return 1;
    }
    // Order is not being changed
    if (newOrder == null && oldOrder != null) {
      return oldOrder;
    }
    short maxOrder = materialGroupRepository.findMaxOrder(courseYearId).orElse((short) -1);
    // No new order specified, default: add it to the end
    if (newOrder == null) {
      return (short) (maxOrder + 1);
    }
    // New order larger than the end position, add it to the end position
    if (newOrder > maxOrder) {
      return (short) (maxOrder + 1);
    }
    // Max is -1 when this is the only element, so no point in incrementing others
    if (maxOrder != -1)
      materialGroupRepository.incrementDisplayOrder(courseYearId, newOrder);
    return newOrder;
  }
}
