package com.andrezzb.coursearchive.material.controllers;

import com.andrezzb.coursearchive.material.dto.comment.CommentCreateDto;
import com.andrezzb.coursearchive.material.dto.comment.CommentUpdateDto;
import com.andrezzb.coursearchive.material.dto.comment.CommentDto;
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

  @GetMapping("/")
  public ResponseEntity<Page<CommentDto>> getCommentsPaged(
    @PositiveOrZero @RequestParam(defaultValue = "0") int page,
    @Positive @RequestParam(defaultValue = "50") int size,
    @ValidEnum(enumClazz = Sort.Direction.class, ignoreCase = true)
    @RequestParam(defaultValue = "asc") List<String> sortDirection,
    @ValidEnum(enumClazz = Comment.SortField.class) @RequestParam(defaultValue = "createdAt")
    List<String> sortField, @RequestParam Long materialId) {
    Pageable p = createPageRequest(page, size, sortField, sortDirection);
    var comments = commentService.findAllCommentsPaged(materialId, p);
    return ResponseEntity.ok(comments);
  }


  @PostMapping("/")
  public ResponseEntity<CommentDto> createComment(
    @Valid @RequestBody CommentCreateDto commentDto) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    var comment = commentService.createComment(commentDto.getMaterialId(), username, commentDto.getText());
    return ResponseEntity.ok(comment);
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
    @Valid @RequestBody CommentUpdateDto commentDto) {
    var comment = commentService.updateComment(commentId, commentDto.getText());
    return ResponseEntity.ok(comment);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
    commentService.deleteComment(commentId);
    return ResponseEntity.noContent().build();
  }

}
