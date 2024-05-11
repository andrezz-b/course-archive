package com.andrezzb.coursearchive.material.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.file.models.MaterialFile;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "material")
public class Material implements AclSecured {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "material_group_id", nullable = false)
  @JsonIgnore
  private MaterialGroup materialGroup;

  @Column(length = 64, nullable = false)
  private String name;

  @Column(length = 512)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "material", orphanRemoval = true,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<MaterialFile> files = new ArrayList<>();

  public void addFile(MaterialFile file) {
    files.add(file);
    file.setMaterial(this);
  }

  public void removeFile(MaterialFile file) {
    files.remove(file);
    file.setMaterial(null);
  }

  @Override
  public Object getParent() {
    return materialGroup;
  }

  public enum FilterField implements FilterValueMapper {
    description, name
  }

  public enum SortField {
    id, name, createdAt, updatedAt
  }
}
