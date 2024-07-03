package com.andrezzb.coursearchive.material.models;

import com.andrezzb.coursearchive.course.models.CourseYear;
import com.andrezzb.coursearchive.security.models.AclSecured;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString(exclude = {"courseYear"})
@Entity
@Table(name = "tag")
public class Tag implements AclSecured {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_year_id", nullable = false)
  @JsonIgnore
  private CourseYear courseYear;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Override
  public AclSecured getParent() {
    return this.getCourseYear();
  }
}
