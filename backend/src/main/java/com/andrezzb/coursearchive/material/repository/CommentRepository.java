package com.andrezzb.coursearchive.material.repository;

import com.andrezzb.coursearchive.material.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findAllByMaterialId(Long materialId, Pageable pageable);
}
