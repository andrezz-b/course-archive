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
import com.andrezzb.coursearchive.program.dto.ProgramDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.services.ProgramService;
import com.andrezzb.coursearchive.validators.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
  public ResponseEntity<Page<ProgramDto>> getAllPrograms(
      @PositiveOrZero @RequestParam(defaultValue = "0") int page,
      @Positive @RequestParam(defaultValue = "5") int size,
      @ValidEnum(enumClazz = Sort.Direction.class,
          ignoreCase = true) @RequestParam(defaultValue = "asc") String sortDirection,
      @ValidEnum(enumClazz = Program.SortField.class) @RequestParam(
          defaultValue = "id") String sortField,
      @ValidEnum(enumClazz = Program.FilterField.class, required = false) @RequestParam(
          required = false) String filterField,
      @RequestParam(required = false) String filterValue,
      @RequestParam(required = false) Long collegeId) {

    Program.FilterField filterFieldEnum =
        filterField != null ? Program.FilterField.valueOf(filterField) : null;
    Object filterValueObj = Program.FilterField.mapFilterValue(filterFieldEnum, filterValue);
    Pageable p =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
    final var programsPaged =
        programService.findAllProgramsPaged(p, filterFieldEnum, filterValueObj, collegeId);
    return ResponseEntity.ok(programsPaged);
  }

  @PostMapping("/")
  public ResponseEntity<Program> createProgram(
      @Valid @RequestBody ProgramCreateDto programCreateDto) {
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
