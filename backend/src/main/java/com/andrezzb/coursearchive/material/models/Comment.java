package com.andrezzb.coursearchive.material.models;

import com.andrezzb.coursearchive.security.models.AclSecured;
import com.andrezzb.coursearchive.security.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment implements AclSecured {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "material_id", nullable = false)
  private Material material;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(nullable = false, length = 1024)
  private String text;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "edited", nullable = false)
  private boolean edited = false;

  public String getUsername() {
    return this.getUser().getUsername();
  }

  public boolean getIsCurrentUser() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return this.getUser().getUsername().equals(username);
  }

  @Override
  public AclSecured getParent() {
    return this.getMaterial();
  }

  @Override
  public Boolean isInheriting() {
    return false;
  }

  public enum SortField {
    createdAt
  }
}
