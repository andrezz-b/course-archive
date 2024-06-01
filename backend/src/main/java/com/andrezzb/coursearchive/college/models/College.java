package com.andrezzb.coursearchive.college.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.security.models.AclSecured;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "college")
public class College implements AclSecured {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 32)
    private String acronym;

    @Column(nullable = false, length = 64)
    private String city;

    @Column(nullable = false)
    private Integer postcode;

    @Column(nullable = false, length = 128)
    private String address;

    @Column(length = 128)
    private String website;

    @Column(length = 512)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Program> programs = new ArrayList<>();

    public void addProgram(Program program) {
        programs.add(program);
        program.setCollege(this);
    }

    public void removeProgram(Program program) {
        programs.remove(program);
        program.setCollege(null);
    }

    @Override
    public AclSecured getParent() {
        return null;
    }

    public enum SortField {
        name, id
    }

    public enum FilterField implements FilterValueMapper {
        name, city, postcode {
            @Override
            public Object map(String filterValue) {
                try {
                    return Integer.parseInt(filterValue);
                } catch (Exception e) {
                    return null;
                }
            }
        },
        acronym
    }
}
