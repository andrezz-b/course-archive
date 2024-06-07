package com.andrezzb.coursearchive.material.controllers;

import com.andrezzb.coursearchive.material.dto.MaterialDto;
import com.andrezzb.coursearchive.material.dto.VoteDto;
import com.andrezzb.coursearchive.material.models.Vote;
import com.andrezzb.coursearchive.material.services.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/material/vote")
public class VoteController {

  private final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  @PostMapping("/{materialId}")
  public ResponseEntity<MaterialDto> vote(@PathVariable Long materialId,
    @Valid @RequestBody VoteDto voteDto) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    var material = voteService.vote(materialId, username, Vote.VoteType.fromString(voteDto.getVoteType()));
    return ResponseEntity.ok(material);
  }
}
