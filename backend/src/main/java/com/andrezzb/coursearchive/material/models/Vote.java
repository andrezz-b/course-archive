package com.andrezzb.coursearchive.material.models;

import com.andrezzb.coursearchive.security.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"material", "user"})
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {

  public enum VoteType {
    UPVOTE, DOWNVOTE;

    public static VoteType fromString(String voteType) {
      return valueOf(voteType.toUpperCase());
    }
  }


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "material_id", nullable = false)
  private Material material;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VoteType voteType;
}
