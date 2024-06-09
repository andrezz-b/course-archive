package com.andrezzb.coursearchive.material.services;

import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.material.exceptions.tag.TagException;
import com.andrezzb.coursearchive.material.exceptions.tag.TagExistsException;
import com.andrezzb.coursearchive.material.exceptions.tag.TagNotFoundException;
import com.andrezzb.coursearchive.material.models.Material;
import com.andrezzb.coursearchive.material.models.Tag;
import com.andrezzb.coursearchive.material.repository.TagRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import org.apache.tika.utils.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

  private final CourseYearService courseYearService;
  private final TagRepository tagRepository;
  private final AclUtilService aclUtilService;

  public TagService(CourseYearService courseYearService, TagRepository tagRepository,
    AclUtilService aclUtilService) {
    this.courseYearService = courseYearService;
    this.tagRepository = tagRepository;
    this.aclUtilService = aclUtilService;
  }

  public Sort getSort(Sort.Direction sortDirection) {
    return Sort.by(sortDirection, "name");
  }

  @PreAuthorize("hasPermission(#courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', 'read')")
  public List<Tag> getTags(Long courseYearId, Sort sort, String name) {
    if (!StringUtils.isBlank(name)) {
      return tagRepository.findAllByCourseYearIdAndNameContainingIgnoreCase(courseYearId, name, sort);
    }
    return tagRepository.findAllByCourseYearId(courseYearId, sort);
  }

  @PreAuthorize("hasPermission(#courseYearId, 'com.andrezzb.coursearchive.course.models.CourseYear', 'create') || hasRole('MANAGER')")
  public Tag createTag(String name, Long courseYearId) {
    var courseYear = courseYearService.findCourseYear(courseYearId);
    var existingTag = courseYear.getTags().stream()
      .filter(tag -> tag.getName().equals(name))
      .findFirst();
    if (existingTag.isPresent()) {
      throw new TagExistsException("Tag already exists with name: " + name);
    }
    var tag = new Tag();
    tag.setName(name);
    tag.setCourseYear(courseYear);
    var savedTag = tagRepository.save(tag);
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    aclUtilService.grantPermission(savedTag, username, AclPermission.ADMINISTRATION);
    return savedTag;
  }

  public Tag updateTag(Long tagId, String name) {
    var tag = tagRepository.findById(tagId)
      .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + tagId));
    var existingTag = tag.getCourseYear().getTags().stream()
      .filter(t -> t.getName().equals(name))
      .findFirst();
    if (existingTag.isPresent()) {
      throw new TagExistsException("Tag already exists with name: " + name);
    }
    tag.setName(name);
    return tagRepository.save(tag);
  }

  @PreAuthorize("hasPermission(#tagId, 'com.andrezzb.coursearchive.material.models.Tag', 'delete') || hasRole('MANAGER')")
  public void deleteTag(Long tagId) {
    long countMaterials = tagRepository.countMaterialsByTagId(tagId);
    if (countMaterials > 0) {
      throw new TagException("Cannot delete tag which is associated with materials");
    }
    tagRepository.deleteById(tagId);
  }

  public void addTagsToMaterial(Material material, List<Long> tagIds) {
    if (tagIds == null || tagIds.isEmpty()) {
      return;
    }
    Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
    Long materialCourseYearId = material.getMaterialGroup().getCourseYear().getId();

    for (Tag tag : tags) {
      Long tagCourseYearId = tag.getCourseYear().getId();
      if (!tagCourseYearId.equals(materialCourseYearId)) {
        throw new TagException("Tag with id: " + tag.getId() + " does not belong to the same course year as the material");
      }
    }

    material.setTags(tags);
  }
}
