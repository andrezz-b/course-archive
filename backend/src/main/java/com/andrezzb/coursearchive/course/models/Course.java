package com.andrezzb.coursearchive.course.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.program.models.Program;
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
@Table(name = "course")
public class Course implements AclSecured {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "program_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Program program;

  @Column(nullable = false, length = 128)
  private String name;

  @Column(nullable = false)
  private Short credits;

  private Short year;

  @Column(length = 32)
  private String acronym;

  @Column(length = 512)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseYear> courseYears = new ArrayList<>();

  public void addCourseYear(CourseYear courseYear) {
    courseYears.add(courseYear);
    courseYear.setCourse(this);
  }

  public void removeCourseYear(CourseYear courseYear) {
    courseYears.remove(courseYear);
    courseYear.setCourse(null);
  }

  @Override
  public Object getParent() {
    return program;
  }

  public static enum FilterField {
    name, credits, year;

    public static Object mapFilterValue(String filterField, String filterValue) {
      var enumValue = FilterField.valueOf(filterField);
      return mapFilterValue(enumValue, filterValue);
    }

    public static Object mapFilterValue(FilterField filterField, String filterValue) {
      if (filterValue == null || filterValue.length() == 0) {
        return null;
      }
      switch (filterField) {
        case credits:
          return Short.parseShort(filterValue);
        case year:
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
