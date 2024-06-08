package com.andrezzb.coursearchive.material.controllers;

import com.andrezzb.coursearchive.material.dto.tag.TagCreateDto;
import com.andrezzb.coursearchive.material.dto.tag.TagUpdateDto;
import com.andrezzb.coursearchive.material.models.Tag;
import com.andrezzb.coursearchive.material.services.TagService;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/material/tag")
public class TagController {

  public final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("/")
  public ResponseEntity<List<Tag>> getTags(@RequestParam Long courseYearId,
    @ValidEnum(enumClazz = Sort.Direction.class, ignoreCase = true)
    @RequestParam(defaultValue = "asc") String sortDirection) {
    var sort = tagService.getSort(Sort.Direction.fromString(sortDirection));
    var tags = tagService.getTags(courseYearId, sort);
    return ResponseEntity.ok(tags);
  }

  @PostMapping("/")
  public ResponseEntity<Tag> createTag(@Valid @RequestBody TagCreateDto tagCreateDto) {
    var tag = tagService.createTag(tagCreateDto.getName(), tagCreateDto.getCourseYearId());
    return ResponseEntity.status(HttpStatus.CREATED).body(tag);
  }

  @PutMapping("/{tagId}")
  public ResponseEntity<Tag> updateTag(@PathVariable Long tagId,
    @Valid @RequestBody TagUpdateDto tagDto) {
    var tag = tagService.updateTag(tagId, tagDto.getName());
    return ResponseEntity.ok(tag);
  }

  @DeleteMapping("/{tagId}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
    tagService.deleteTag(tagId);
    return ResponseEntity.noContent().build();
  }
}
