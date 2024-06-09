package com.andrezzb.coursearchive.material.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.repository.FilterValueMapper;
import com.andrezzb.coursearchive.security.models.AclSecured;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@ToString(exclude = "materialGroup")
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

  @ManyToMany
  @JoinTable(name = "material_tag", joinColumns = @JoinColumn(name = "material_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
  private Set<Tag> tags = new HashSet<>();

  @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vote> votes = new ArrayList<>();

  public void addVote(Vote vote) {
    votes.add(vote);
    vote.setMaterial(this);
  }

  public void removeVote(Vote vote) {
    votes.remove(vote);
    vote.setMaterial(null);
  }

  public Vote.VoteType getCurrentUserVote() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return votes.stream().filter(v -> v.getUser().getUsername().equals(username))
      .map(Vote::getVoteType).findFirst().orElse(null);
  }

  public int getVoteCount() {
    int upVotes = (int) votes.stream().filter(v -> v.getVoteType() == Vote.VoteType.UPVOTE).count();
    int downVotes =
      (int) votes.stream().filter(v -> v.getVoteType() == Vote.VoteType.DOWNVOTE).count();
    return upVotes - downVotes;
  }

  @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setMaterial(this);
  }

  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setMaterial(null);
  }

  public void addFile(MaterialFile file) {
    files.add(file);
    file.setMaterial(this);
  }

  public void removeFile(MaterialFile file) {
    files.remove(file);
    file.setMaterial(null);
  }

  @Override
  public AclSecured getParent() {
    return materialGroup;
  }

  public enum FilterField implements FilterValueMapper {
    description, name
  }


  public enum SortField {
    id, name, createdAt, updatedAt
  }
}
