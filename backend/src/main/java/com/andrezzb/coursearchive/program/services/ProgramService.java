package com.andrezzb.coursearchive.program.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.andrezzb.coursearchive.college.services.CollegeService;
import com.andrezzb.coursearchive.program.dto.ProgramCreateDto;
import com.andrezzb.coursearchive.program.dto.ProgramUpdateDto;
import com.andrezzb.coursearchive.program.exceptions.ProgramNotFoundException;
import com.andrezzb.coursearchive.program.models.Program;
import com.andrezzb.coursearchive.program.repository.ProgramRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProgramService {

  private final ProgramRepository programRepository;
  private final CollegeService collegeService;
  private final ModelMapper modelMapper;


  public ProgramService(ProgramRepository programRepository, CollegeService collegeService,
      ModelMapper modelMapper) {
    this.programRepository = programRepository;
    this.collegeService = collegeService;
    this.modelMapper = modelMapper;
  }

  public Page<Program> getAllProgramsPaged(Pageable p) {
    return programRepository.findAll(p);
  }

  public Program findProgramById(Long id) {
    return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException(id));
  }

  public Program createProgram(ProgramCreateDto programDto) {
    var college = collegeService.findCollegeById(programDto.getCollegeId());
    Program program = modelMapper.map(programDto, Program.class);
    program.setCollege(college);
    return programRepository.save(program);
  }

  public Program updateProgram(Long id, @Valid ProgramUpdateDto programUpdateDto) {
    Program program = findProgramById(id);
    modelMapper.map(programUpdateDto, program);
    return programRepository.save(program);
  }

  public void deleteProgramById(Long id) {
    programRepository.deleteById(id);
  }

}
