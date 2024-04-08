package com.andrezzb.coursearchive.college.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.security.models.AclSecured;
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
    public Object getParent() {
        return null;
    }
}
