package com.andrezzb.coursearchive.material.services;

import com.andrezzb.coursearchive.material.dto.comment.CommentDto;
import com.andrezzb.coursearchive.material.exceptions.CommentNotFoundException;
import com.andrezzb.coursearchive.material.models.Comment;
import com.andrezzb.coursearchive.material.repository.CommentRepository;
import com.andrezzb.coursearchive.security.acl.AclPermission;
import com.andrezzb.coursearchive.security.services.AclUtilService;
import com.andrezzb.coursearchive.security.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  private final UserService userService;
  private final MaterialService materialService;
  private final ModelMapper modelMapper;
  private final CommentRepository commentRepository;
  private final AclUtilService aclUtilService;

  public CommentService(UserService userService, MaterialService materialService,
    ModelMapper modelMapper, CommentRepository commentRepository, AclUtilService aclUtilService) {
    this.userService = userService;
    this.materialService = materialService;
    this.modelMapper = modelMapper;
    this.commentRepository = commentRepository;
    this.aclUtilService = aclUtilService;
  }

  public Comment findCommentById(Long commentId) {
    return commentRepository.findById(commentId)
      .orElseThrow(() -> new CommentNotFoundException(commentId));
  }

  @PreAuthorize(
    "hasPermission(#materialId, 'com.andrezzb.coursearchive.material.models.Material', 'READ')")
  public Page<CommentDto> findAllCommentsPaged(Long materialId, Pageable p) {
    var comments = commentRepository.findAllByMaterialId(materialId, p);
    return comments.map(comment -> modelMapper.map(comment, CommentDto.class));
  }

  @PreAuthorize(
    "hasPermission(#materialId, 'com.andrezzb.coursearchive.material.models.Material', 'READ')")
  public CommentDto createComment(Long materialId, String username, String text) {
    var user = userService.findByUsername(username);
    var material = materialService.findMaterialById(materialId);
    Comment comment = new Comment();
    comment.setUser(user);
    comment.setMaterial(material);
    comment.setText(text);

    var savedComment = commentRepository.save(comment);
    aclUtilService.grantPermission(savedComment, username, AclPermission.ADMINISTRATION);
    return modelMapper.map(savedComment, CommentDto.class);
  }

  @PreAuthorize(
    "hasPermission(#commentId, 'com.andrezzb.coursearchive.material.models.Comment', 'WRITE')")
  public CommentDto updateComment(Long commentId, String newText) {
    var comment = findCommentById(commentId);
    comment.setText(newText);
    comment.setEdited(true);
    var savedComment = commentRepository.save(comment);
    return modelMapper.map(savedComment, CommentDto.class);
  }

  @PreAuthorize(
    "hasPermission(#commentId, 'com.andrezzb.coursearchive.material.models.Comment', 'DELETE')")
  public void deleteComment(Long commentId) {
    commentRepository.deleteById(commentId);
  }


}
