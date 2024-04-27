package com.andrezzb.coursearchive.course.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.material.models.MaterialGroup;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "course_year",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "academic_year"}))
public class CourseYear implements AclSecured {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @Column(nullable = false, length = 9, name = "academic_year")
    private String academicYear;

    private String professor;
    private String assistant;
    private Short difficulty;

    @Column(name = "enrollment_count")
    private Short enrollmentCount;

    @Column(name = "passed_count")
    private Short passedCount;

    @Column(name = "lecture_count")
    private Short lectureCount;

    @Column(name = "exercise_count")
    private Short exerciseCount;

    @Column(name = "laboratory_count")
    private Short laboratoryCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "courseYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialGroup> materialGroups = new ArrayList<>();

    public void addMaterialGroup(MaterialGroup materialGroup) {
        materialGroups.add(materialGroup);
        materialGroup.setCourseYear(this);
    }

    public void removeMaterialGroup(MaterialGroup materialGroup) {
        materialGroups.remove(materialGroup);
        materialGroup.setCourseYear(null);
    }

    @Override
    public Object getParent() {
        return course;
    }
}