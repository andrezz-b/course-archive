package com.andrezzb.coursearchive.program.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.services.ProgramService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/program")
public class ProgramController {
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @GetMapping("/{id}")
  private ResponseEntity<Program> getProgramById(@PathVariable Long id) {
    final var program = programService.findProgramById(id);
    return ResponseEntity.ok(program);
  }

  @PostMapping("/")
  public ResponseEntity<Long> createProgram(@Valid @RequestBody ProgramCreateDto programCreateDto) {
      var program = programService.createProgram(programCreateDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(program.getId());
  }
  
}
