package com.andrezzb.coursearchive.material.services;

import com.andrezzb.coursearchive.material.dto.MaterialDto;
import com.andrezzb.coursearchive.material.repository.MaterialRepository;
import com.andrezzb.coursearchive.security.services.UserService;
import com.andrezzb.coursearchive.material.models.Vote;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

  private final MaterialService materialService;
  private final MaterialRepository materialRepository;
  private final UserService userService;
  private final ModelMapper modelMapper;

  public VoteService(MaterialService materialService, MaterialRepository materialRepository, UserService userService, ModelMapper modelMapper) {
    this.materialService = materialService;
    this.materialRepository = materialRepository;
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  @Transactional
  public MaterialDto vote(Long materialId, String username, Vote.VoteType voteType) {
    var material = materialService.findMaterialById(materialId);
    var user = userService.findByUsername(username);
    Optional<Vote> existingVote = material.getVotes().stream()
      .filter(vote -> vote.getUser().getId().equals(user.getId()))
      .findFirst();

    if (existingVote.isPresent()) {
      var vote = existingVote.get();
      if (vote.getVoteType() == voteType) {
        material.removeVote(vote);
      } else {
        vote.setVoteType(voteType);
      }
    } else {
      // Add a new vote
      Vote vote = new Vote();
      vote.setMaterial(material);
      vote.setUser(user);
      vote.setVoteType(voteType);
      material.addVote(vote);
    }

    material.setVoteCount(material.calculateVoteCount());

    return modelMapper.map(materialRepository.save(material), MaterialDto.class);
  }
}
