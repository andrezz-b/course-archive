package com.andrezzb.coursearchive.program.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.college.models.College;
import com.andrezzb.coursearchive.course.models.Course;
import com.andrezzb.coursearchive.security.models.AclSecured;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "program")
public class Program implements AclSecured {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "college_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private College college;

  @Column(nullable = false, length = 128)
  private String name;

  @Column(nullable = false)
  private Short duration;

  @Column(name = "degree_type", length = 64)
  private String degreeType;

  @Column(name = "degree_title", length = 128)
  private String degreeTitle;

  @Column(name = "degree_title_abrev", length = 64)
  private String degreeTitleAbbreviation;

  @Column(length = 512)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();

  public void addCourse(Course course) {
    courses.add(course);
    course.setProgram(this);
  }

  public void removeCourse(Course course) {
    courses.remove(course);
    course.setProgram(null);
  }

  @Override
  public Object getParent() {
    return college;
  }

  public static enum FilterField {
    name, duration;

    public static Object mapFilterValue(FilterField filterField, String filterValue) {
      if (filterValue == null || filterValue.length() == 0) {
        return null;
      }
      switch (filterField) {
        case duration:
          return Short.parseShort(filterValue);
        default:
          return filterValue;
      }
    }
  }

  public static enum SortField {
    id, name
  }

}
