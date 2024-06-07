package com.andrezzb.coursearchive.material.controllers;

import com.andrezzb.coursearchive.material.dto.CommentDataDto;
import com.andrezzb.coursearchive.material.dto.CommentDto;
import com.andrezzb.coursearchive.material.models.Comment;
import com.andrezzb.coursearchive.material.services.CommentService;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.andrezzb.coursearchive.utils.PageRequestUtils.createPageRequest;

@RestController
@RequestMapping("api/material/comment")
public class CommentController {
  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping("/{materialId}")
  public ResponseEntity<Page<CommentDto>> getCommentsPaged(@PathVariable Long materialId,
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "5") int size,
    @ValidEnum(enumClazz = Sort.Direction.class, ignoreCase = true)
    @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = Comment.SortField.class) @RequestParam(defaultValue = "createdAt")
    List<String> sortField) {
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    var comments = commentService.findAllCommentsPaged(materialId, p);
    return ResponseEntity.ok(comments);
  }


  @PostMapping("/{materialId}")
  public ResponseEntity<CommentDto> createComment(@PathVariable Long materialId, @Valid @RequestBody
    CommentDataDto commentDto) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    var comment = commentService.createComment(materialId, username, commentDto.getText());
    return ResponseEntity.ok(comment);
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @Valid @RequestBody
  CommentDataDto commentDto) {
    var comment = commentService.updateComment(commentId, commentDto.getText());
    return ResponseEntity.ok(comment);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
    commentService.deleteComment(commentId);
    return ResponseEntity.noContent().build();
  }

}
