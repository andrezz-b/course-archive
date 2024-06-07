package com.andrezzb.coursearchive.material.dto;

import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.material.models.Vote;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MaterialDto {
  private Long id;
  private String name;
  private String description;
  private List<MaterialFile> files = new ArrayList<>();
  private int voteCount;
  private Vote.VoteType currentUserVote;
}
