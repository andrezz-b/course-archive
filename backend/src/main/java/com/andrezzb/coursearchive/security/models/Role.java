package com.andrezzb.coursearchive.security.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<UserEntity> posts = new HashSet<>();

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Role role = (Role) obj;
    return name.equals(role.name);
  }

  public enum RoleName {
    USER, MANAGER, ADMIN;

    public static RoleName fromString(String roleName) {
      return RoleName.valueOf(roleName.toUpperCase());
    }
  }
}
