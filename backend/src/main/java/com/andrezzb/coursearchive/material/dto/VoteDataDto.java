package com.andrezzb.coursearchive.material.dto;

import com.andrezzb.coursearchive.material.models.Vote;
import com.andrezzb.coursearchive.validators.ValidEnum;
import lombok.Data;

@Data
public class VoteDataDto {
  @ValidEnum(enumClazz = Vote.VoteType.class, ignoreCase = true)
  private String voteType;
}
