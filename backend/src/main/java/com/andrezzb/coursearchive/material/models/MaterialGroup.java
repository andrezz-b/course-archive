package com.andrezzb.coursearchive.material.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.course.models.CourseYear;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
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
@Table(name = "material_group",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_year_id", "name"}))
public class MaterialGroup implements AclSecured {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_year_id", nullable = false)
    @JsonIgnore
    private CourseYear courseYear;

    @Column(nullable = false)
    private Short displayOrder;

    // @Column(nullable = false, length = 32)
    // private String type;

    @Column(length = 512)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "materialGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials = new ArrayList<>();

    public void addMaterial(Material material) {
        materials.add(material);
        material.setMaterialGroup(this);
    }

    public void removeMaterial(Material material) {
        materials.remove(material);
        material.setMaterialGroup(null);
    }

    @Override
    public Object getParent() {
        return courseYear;
    }

    public enum FilterField implements FilterValueMapper {
        name
    }

    public enum SortField {
        name, displayOrder
    }
}
