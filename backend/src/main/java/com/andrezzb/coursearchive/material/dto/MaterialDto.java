package com.andrezzb.coursearchive.material.dto;

import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.material.models.Tag;
import com.andrezzb.coursearchive.material.models.Vote;
import lombok.Data;

import java.util.*;

@Data
public class MaterialDto {
  private Long id;
  private String name;
  private String description;
  private List<MaterialFile> files = new ArrayList<>();
  private Set<Tag> tags = new LinkedHashSet<>();
  private int voteCount;
  private Vote.VoteType currentUserVote;

  public void setTags(Collection<Tag> tags) {
    this.tags = new LinkedHashSet<>(tags);
  }
}
