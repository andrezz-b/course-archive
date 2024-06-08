package com.andrezzb.coursearchive.material.services;

import com.andrezzb.coursearchive.course.services.CourseYearService;
import com.andrezzb.coursearchive.material.exceptions.tag.TagException;
import com.andrezzb.coursearchive.material.exceptions.tag.TagExistsException;
import com.andrezzb.coursearchive.material.exceptions.tag.TagNotFoundException;
import com.andrezzb.coursearchive.material.models.Tag;
import com.andrezzb.coursearchive.material.repository.TagRepository;
import org.apache.tika.utils.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

  private final CourseYearService courseYearService;
  private final TagRepository tagRepository;

  public TagService(CourseYearService courseYearService, TagRepository tagRepository) {
    this.courseYearService = courseYearService;
    this.tagRepository = tagRepository;
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
    return tagRepository.save(tag);
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
}
