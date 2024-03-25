package com.andrezzb.coursearchive.program.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.services.ProgramService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/program")
public class ProgramController {
  private final ProgramService programService;

  public ProgramController(ProgramService programService) {
    this.programService = programService;
  }

  @GetMapping("/")
  public ResponseEntity<Page<Program>> getAllPrograms(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "asc") String sortDirection,
      @RequestParam(defaultValue = "id") String sortField) {
    Sort.Direction direction =
        sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable p = PageRequest.of(page, size, Sort.by(direction, sortField));
    final var programsPaged = programService.getAllProgramsPaged(p);
    return ResponseEntity.ok(programsPaged);
  }

  @PostMapping("/")
  public ResponseEntity<Program> createProgram(@Valid @RequestBody ProgramCreateDto programCreateDto) {
    var program = programService.createProgram(programCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(program);
  }

  @GetMapping("/{id}")
  private ResponseEntity<Program> getProgramById(@PathVariable Long id) {
    final var program = programService.findProgramById(id);
    return ResponseEntity.ok(program);
  }

  @PutMapping("/{id}")
  private ResponseEntity<Program> updateProgramById(@PathVariable Long id,
      @Valid @RequestBody ProgramUpdateDto programUpdateDto) {
    var program = programService.updateProgram(id, programUpdateDto);
    return ResponseEntity.status(HttpStatus.OK).body(program);
  }

  @DeleteMapping("/{id}")
  private ResponseEntity<Void> deleteProgramById(@PathVariable Long id) {
    programService.deleteProgramById(id);
    return ResponseEntity.noContent().build();
  }


}
