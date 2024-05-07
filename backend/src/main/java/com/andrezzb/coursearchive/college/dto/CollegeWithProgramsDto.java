package com.andrezzb.coursearchive.college.dto;

import java.util.List;
import com.andrezzb.coursearchive.program.dto.ProgramDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollegeWithProgramsDto extends CollegeDto {
  
  List<ProgramDto> programs;
}
