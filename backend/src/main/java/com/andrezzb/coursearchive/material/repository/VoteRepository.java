package com.andrezzb.coursearchive.material.repository;

import com.andrezzb.coursearchive.material.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
