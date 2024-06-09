package com.andrezzb.coursearchive.material.dto;

import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.material.models.Tag;
import com.andrezzb.coursearchive.material.models.Vote;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MaterialDto {
  private Long id;
  private String name;
  private String description;
  private List<MaterialFile> files = new ArrayList<>();
  private Set<Tag> tags = new HashSet<>();
  private int voteCount;
  private Vote.VoteType currentUserVote;
}
